/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.learn.index12306.framework.starter.distributedid.core.snowflake;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import org.learn.index12306.framework.starter.distributedid.core.IdGenerator;

import java.io.Serializable;
import java.util.Date;


/**
 * 雪花算法，生成全局唯一的 ID
 *
 * <pre>
 * 符号位（1bit）- 时间戳相对值（41bit）- 数据中心标志（5bit）- 机器标志（5bit）- 递增序号（12bit）
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * 第一位为未使用(符号位表示正数)，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 * <p>
 *
 * @author Milk
 * @version 2023/9/23 14:16
 */
public class Snowflake implements Serializable, IdGenerator {

    private static final long serialVersionUID = 1L;

    /**
     * 默认的起始时间，为Thu, 04 Nov 2010 01:42:54 GMT
     */
    private static long DEFAULT_TWEPOCH = 1288834974657L;

    /**
     * 默认回拨时间 2s
     */
    private static long DEFAULT_TIME_OFFSET = 5L;

    private static final long WORKER_ID_BITS = 5L;

    // 最大支持 5bit,最大支持机器点数31个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private static final long MAX_WORKED_ID = -1L ^ (-1L << WORKER_ID_BITS);

    private static final long DATA_CENTER_ID_BITS = 5L;

    // 最大支持 5bit，最大支持数据中心31个
    @SuppressWarnings({"PointlessBitwiseExpression", "FieldCanBeLocal"})
    private static final long MAX_DATA_CENTER_ID = -1L ^ (-1L << DATA_CENTER_ID_BITS);

    // 序列号12位（表示只允许workId的范围为：0-4095）
    private static final long SEQUENCE_BITS = 12L;

    // 机器节点左移12位
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 数据中心节点左移17位
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    // 时间毫秒数左移22位
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;

    // 序列掩码，用于限定序列最大值不能超过4095
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 初始化时间点
     */
    private final long twepoch;

    private final long workerId;

    private final long dataCenterId;

    private final boolean useSystemClock;

    /**
     * 允许的时钟回拨毫秒数
     */
    private final long timeOffset;

    /**
     * 当在低频模式下时，序号始终为0，导致生成ID始终为偶数<br>
     * 此属性用于限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题。<br>
     * 注意次数必须小于{@link #SEQUENCE_MASK}，{@code 0}表示不使用随机数。<br>
     * 这个上限不包括值本身。
     */
    private final long randomSequenceLimit;

    /**
     * 自增序号，当高频模式下时，同一毫秒内生成N个ID，则这个序号在同一毫秒下，自增以避免ID重复。
     */

    private long sequence = 0L;

    private long lastTimestamp = -1L;



    /**
     * 构造，使用自动生成的工作节点ID和数据中心ID
     */
    public Snowflake(){
        this(IdUtil.getWorkerId(IdUtil.getDataCenterId(MAX_DATA_CENTER_ID), MAX_WORKED_ID));
    }

    /**
     * @param workerId 终端ID
     */
    public Snowflake(long workerId){
        this(workerId, IdUtil.getDataCenterId(MAX_DATA_CENTER_ID));
    }

    /**
     * @param workerId             工作机器节点Id
     * @param dataCenterId         数据中心Id
     */
    public Snowflake(long dataCenterId, long workerId){
        this(dataCenterId, workerId, false);
    }

    /**
     * @param workerId             工作机器节点Id
     * @param dataCenterId         数据中心Id
     * @param isUseSystemClock     是否使用{@link SystemClock} 获取当前时间戳
     */
    public Snowflake(long dataCenterId, long workerId, boolean isUseSystemClock ){
        this(null,dataCenterId, workerId, isUseSystemClock);
    }


    /**
     * @param epochDate        初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId         工作机器节点id
     * @param dataCenterId     数据中心id
     * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     */
    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, DEFAULT_TIME_OFFSET);
    }

    /**
     * @param epochDate        初始化时间起点（null表示默认起始日期）,后期修改会导致id重复,如果要修改连workerId dataCenterId，慎用
     * @param workerId         工作机器节点id
     * @param dataCenterId     数据中心id
     * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     * @param timeOffset       允许时间回拨的毫秒数
     */
    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock, long timeOffset) {
        this(epochDate, workerId, dataCenterId, isUseSystemClock, timeOffset, 0);
    }

    /**
     *
     * @param epochDate            初始化时间起点，后期修改会导致 ID 重复，需要将 workId, dataCenterId 同时修改
     * @param workerId             工作机器节点Id
     * @param dataCenterId         数据中心Id
     * @param isUseSystemClock     是否使用{@link SystemClock} 获取当前时间戳
     * @param timeOffset           允许时间回拨的毫秒数
     * @param randomSequenceLimit  限定一个随机上限，在不同毫秒下生成序号时，给定一个随机数，避免偶数问题，0表示无随机，上限不包括值本身。
     */
    public Snowflake(Date epochDate, long workerId, long dataCenterId, boolean isUseSystemClock, long timeOffset, long randomSequenceLimit){
        this.twepoch = (null != epochDate) ? epochDate.getTime() : DEFAULT_TWEPOCH;
        this.workerId = Assert.checkBetween(workerId, 0, MAX_WORKED_ID);
        this.dataCenterId = Assert.checkBetween(dataCenterId, 0, MAX_DATA_CENTER_ID);
        this.useSystemClock = isUseSystemClock;
        this.timeOffset = timeOffset;
        this.randomSequenceLimit = Assert.checkBetween(randomSequenceLimit, 0, SEQUENCE_MASK);

    }


    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> WORKER_ID_SHIFT & ~(-1L << WORKER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> DATA_CENTER_ID_SHIFT & ~(-1L << DATA_CENTER_ID_BITS);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return (id >> TIMESTAMP_LEFT_SHIFT & ~(-1L << 41L)) + twepoch;
    }


    /**
     * 下一个 ID
     *
     * @return ID
     */
    @Override
    public synchronized long nextId(){
        long timestamp = genTime();
        // 如果当前时间比之前的时间小，即时间错误，发生了倒退
        if(timestamp < this.lastTimestamp){
            if(this.lastTimestamp - timestamp < timeOffset){
                // 容忍指定的回拨，避免NTP校时造成的异常
                timestamp = lastTimestamp;
            }else{
                throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
            }
        }
        if(timestamp == this.lastTimestamp){
            final long sequence = (this.sequence + 1) & SEQUENCE_MASK;
            if(sequence == 0){
                timestamp = tilNextMillis(lastTimestamp);
            }
            this.sequence = sequence;
        }else{
            if(randomSequenceLimit > 1){
                sequence = RandomUtil.randomLong(randomSequenceLimit);
            }else {
                sequence = 0L;
            }
        }
        lastTimestamp = timestamp;
        // 组装 ID
        return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT) | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;

    }

    /**
     * 下一个 ID(String)
     *
     * @return ID
     */
    @Override
    public String nextIdStr(){
        return Long.toString(nextId());
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private long genTime(){
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }


    /**
     * 获取生成下一个 ID 的时间戳
     */
    private long tilNextMillis(long lastTimestamp){
        long timestamp = genTime();
        // 监测时间变化
        while(timestamp == lastTimestamp){
            timestamp = genTime();
        }
        // 发现系统时间倒退
        if(timestamp < lastTimestamp){
            throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
        }
        return timestamp;
    }

    /**
     * 解析雪花算法生成的 ID 为对象
     *
     * @param snowflakeId 雪花算法 ID
     */
    public SnowflakeIdInfo parseSnowflakeId(long snowflakeId) {
        return SnowflakeIdInfo.builder().sequence((int) (snowflakeId & ~(-1L << SEQUENCE_BITS))).workerId((int) ((snowflakeId >> WORKER_ID_SHIFT)
                        & ~(-1L << WORKER_ID_BITS))).dataCenterId((int) ((snowflakeId >> DATA_CENTER_ID_SHIFT)
                        & ~(-1L << DATA_CENTER_ID_BITS)))
                .timestamp((snowflakeId >> TIMESTAMP_LEFT_SHIFT) + twepoch).build();
    }

}
