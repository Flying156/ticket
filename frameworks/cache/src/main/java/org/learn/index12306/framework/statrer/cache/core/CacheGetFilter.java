package org.learn.index12306.framework.statrer.cache.core;

/**
 * 缓存过滤
 *
 * @author Milk
 * @version 2023/9/26 17:09
 */
public interface CacheGetFilter<T> {

    /**
     * 缓存过滤
     *
     * @param param 输出参数
     * @return {@code true} 如果输入参数匹配，否则 {@link Boolean#TRUE}
     */
    boolean filter(T param);
}
