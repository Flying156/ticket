package org.learn.index12306.framework.statrer.cache.core;

/**
 * 缓存为空
 *
 * @author Milk
 * @version 2023/9/19 19:37
 */
public interface CacheGetIfAbsent<T> {

    /**
     * 如果查询结果为空，执行逻辑
     */
    void execute(T param);
}
