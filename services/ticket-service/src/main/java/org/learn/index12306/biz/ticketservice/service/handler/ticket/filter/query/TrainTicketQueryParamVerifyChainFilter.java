package org.learn.index12306.biz.ticketservice.service.handler.ticket.filter.query;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dao.entity.RegionDO;
import org.learn.index12306.biz.ticketservice.dao.entity.StationDO;
import org.learn.index12306.biz.ticketservice.dao.mapper.RegionMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.StationMapper;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.LOCK_QUERY_ALL_REGION_LIST;
import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.QUERY_ALL_REGION_LIST;

/**
 * 查询列车车票流程过滤器之验证数据是否正确
 *
 * @author Milk
 * @version 2023/10/9 17:22
 */
@Component
@RequiredArgsConstructor
public class TrainTicketQueryParamVerifyChainFilter implements TrainTicketQueryChainFilter<TicketPageQueryReqDTO> {

    private final RegionMapper regionMapper;
    private final StationMapper stationMapper;
    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;

    /**
     * 缓存数据为空并且已经加载过标识
     */
    private static boolean CACHE_DATA_ISNULL_AND_LOAD_FLAG = false;

    @Override
    public void handler(TicketPageQueryReqDTO requestParam) {
        StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
        HashOperations<String, Object, Object> hashOperations = instance.opsForHash();
        // 查询出发站点和到达站点是否存在
        List<Object> actualExistList = hashOperations.multiGet(
                QUERY_ALL_REGION_LIST,
                ListUtil.toList(requestParam.getFromStation(), requestParam.getToStation())
        );
        long emptyCount = actualExistList.stream().filter(Objects::isNull).count();
        // 如果都存在表示都存在
        if(emptyCount == 0L){
            return;
        }
        // 出发站和终点站都为空且缓存已经加载过，缓存键存在
        // 出发站和终点站有一个为空
        if((emptyCount == 2L && CACHE_DATA_ISNULL_AND_LOAD_FLAG && distributedCache.hasKey(QUERY_ALL_REGION_LIST))
            || emptyCount == 1L){
            throw new ClientException("出发地或目的地不存在");
        }
        // 从数据库查询
        RLock lock = redissonClient.getLock(LOCK_QUERY_ALL_REGION_LIST);
        lock.lock();
        try{
            // 获取完分布式锁，避免重复且无用的加载数据库，通过双重判定锁的形式，在查询一次缓存
            // 如果缓存存在，直接返回即可。
            if(distributedCache.hasKey(QUERY_ALL_REGION_LIST)){
                actualExistList = hashOperations.multiGet(
                        QUERY_ALL_REGION_LIST,
                        ListUtil.toList(requestParam.getFromStation(), requestParam.getToStation())
                );
                emptyCount = actualExistList.stream().filter(Objects::isNull).count();
                if(emptyCount != 2L){
                    throw new ClientException("出发地或目的地不存在");

                }
                return;
            }
            List<RegionDO> regionDOList = regionMapper.selectList(Wrappers.emptyWrapper());
            List<StationDO> stationDOList = stationMapper.selectList(Wrappers.emptyWrapper());
            HashMap<Object, Object>regionValueMap = Maps.newHashMap();
            // 将代号和名称存放哈希表中
            for(RegionDO each : regionDOList){
                regionValueMap.put(each.getCode(), each.getName());
            }
            for(StationDO each : stationDOList){
                regionValueMap.put(each.getCode(), each.getName());
            }
            hashOperations.putAll(QUERY_ALL_REGION_LIST, regionValueMap);
            CACHE_DATA_ISNULL_AND_LOAD_FLAG = true;
            // 判断是否存在出发地和目的地
            emptyCount = regionValueMap.keySet().stream()
                    .filter(each -> StrUtil.containsAny(each.toString(), requestParam.getFromStation(), requestParam.getToStation()))
                    .count();
            if(emptyCount != 2L){
                throw new ClientException("出发地或目的地不存在");
            }
        }finally {
            lock.unlock();
        }


    }
    @Override
    public int getOrder() {
        return 20;
    }
}
