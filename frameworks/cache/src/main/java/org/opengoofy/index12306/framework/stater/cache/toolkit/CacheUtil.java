package org.opengoofy.index12306.framework.stater.cache.toolkit;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * 缓存工具类
 *
 * @author Milk
 * @version 2023/9/19 20:01
 */
public final class CacheUtil {

    public static final String SPLICING_OPERATOR = "_";


    /**
     * 构建缓存 Key
     */
    public static String buildKey(String ...keys){
        Stream.of(keys).forEach(each -> Optional.ofNullable(Strings.emptyToNull(each)).orElseThrow(()-> new RuntimeException("构建缓存键值不能为空")));
        return Joiner.on(SPLICING_OPERATOR).join(keys);
    }


    /**
     * 判断结果是否为空或字符串
     */
    public static boolean isNullOrBlank(Object cacheVal){
        return cacheVal == null || (cacheVal instanceof String && Strings.isNullOrEmpty((String)cacheVal));
    }

}
