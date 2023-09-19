package org.opengoofy.index12306.framework.stater.cache;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;

/**
 * 缓存操作接口
 *
 * @author Milk
 * @version 2023/9/19 20:09
 */
public interface Cache {

    /**
     * 获取缓存
     */
    <T> T get(@NotBlank String key, Class<T>clazz);

    /**
     * 添加缓存
     */
    void put(@NotBlank String key, Object value);

    /**
     * 判断 keys 是否全部存在
     */
    Boolean putIfAllAbsent(@NotNull Collection<String>keys);

    /**
     * 删除缓存
     */
    Boolean delete(@NotBlank String key);

    /**
     * 删除 keys, 返回删除数量
     */
    Long delete(@NotNull Collection<String>keys);

    /**
     * 判断 Key 是否存在
     */
    Boolean hasKey(@NotBlank String key);

    /**
     * 获取缓存组件实例
     */
    Object getInstance();
}
