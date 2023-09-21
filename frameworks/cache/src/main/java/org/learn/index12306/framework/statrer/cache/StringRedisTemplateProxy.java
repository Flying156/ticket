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
