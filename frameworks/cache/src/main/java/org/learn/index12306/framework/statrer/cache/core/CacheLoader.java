package org.learn.index12306.framework.statrer.cache.core;

/**
 * 缓存加载器
 *
 * @author Milk
 * @version 2023/9/19 22:13
 */

@FunctionalInterface
public interface CacheLoader<T> {

    /**
     * 加载缓存
     */
    T load();
}
