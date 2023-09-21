package org.learn.index12306.framework.statrer.cache;

import jakarta.validation.constraints.NotBlank;
import org.learn.index12306.framework.statrer.cache.core.CacheLoader;

/**
 * 分布式缓存操作
 *
 * @author Milk
 * @version 2023/9/19 20:47
 */
public interface DistributedCache extends Cache{


    /**
     * 获取缓存，如查询结果为空，调用 {@link CacheLoader} 加载缓存
     */
    <T> T get(@NotBlank String key, Class<T> clazz, CacheLoader<T> cacheLoader, Long timeout);
}
