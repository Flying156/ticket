package org.learn.index12306.framework.starter.idempotent.core;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 幂等上下文
 *
 * @author Milk
 * @version 2023/10/6 9:38
 */
public final class IdempotentContext {

    private static final ThreadLocal<Map<String, Object>> CONTEXT = new ThreadLocal<>();

    public static Map<String, Object> get(){return CONTEXT.get();}


    public static Object getKey(String key){
        Map<String, Object> context = get();
        if(CollUtil.isNotEmpty(context)){
            return context.get(key);
        }
        return null;
    }

    public static String getStringKey(String key){
        Object actual = getKey(key);
        if (actual != null) {
            return actual.toString();
        }
        return null;
    }


    public static void put(String key, Object value){
        Map<String, Object> context = get();
        if(CollUtil.isEmpty(context)){
            context = Maps.newHashMap();
        }
        context.put(key, value);
        putContext(context);
    }

    public static void putContext(Map<String, Object> context){
        Map<String, Object>threadContext = get();
        if(CollUtil.isNotEmpty(threadContext)){
            threadContext.putAll(context);
            return;
        }
        CONTEXT.set(context);
    }

    public static void clean(){
        CONTEXT.remove();
    }



}
