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

package org.learn.index12306.framework.statrer.cache;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.framework.statrer.cache.config.RedisDistributedProperties;
import org.learn.index12306.framework.statrer.cache.core.CacheLoader;
import org.learn.index12306.framework.statrer.cache.toolkit.FastJson2Util;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Collection;

/**
 * 分布式缓存操作 Redis 模版代理
 * 底层通过 {@link RedissonClient}、{@link StringRedisTemplate} 完成外观接口行为
 *
 * @author Milk
 * @version 2023/9/19 20:53
 */
@RequiredArgsConstructor
public class StringRedisTemplateProxy implements DistributedCache {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedisDistributedProperties redisDistributedProperties;
    private final RedissonClient redissonClient;


    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value = stringRedisTemplate.opsForValue().get(key);
        // clazz 如果是 String 子类
        if(String.class.isAssignableFrom(clazz)){
            return (T)value;
        }
        return JSON.parseObject(value, FastJson2Util.buildType(clazz));
    }

    @Override
    public void put(String key, Object value) {
    }

    @Override
    public Boolean putIfAllAbsent(Collection<String> keys) {
        return null;
    }

    @Override
    public Boolean delete(String key) {
        return stringRedisTemplate.delete(key);
    }

    @Override
    public Long delete(Collection<String> keys) {
        return stringRedisTemplate.delete(keys);
    }

    @Override
    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    @Override
    public Object getInstance() {
        return null;
    }

    @Override
    public <T> T get(String key, Class<T> clazz, CacheLoader<T> cacheLoader, Long timeout) {
        return null;
    }
}
