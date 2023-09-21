package org.learn.index12306.framework.statrer.cache.toolkit;

import com.alibaba.fastjson2.util.ParameterizedTypeImpl;

import java.lang.reflect.Type;

/**
 * FastJson 工具类
 *
 * @author Milk
 * @version 2023/9/19 19:40
 */
public final class FastJson2Util {

    /**
     * 构建类型
     */
    public static Type buildType(Type ...types){
        ParameterizedTypeImpl beforeType = null;
        if(types != null && types.length > 0){
            if(types.length == 1){
                return new ParameterizedTypeImpl(new Type[]{null}, null, types[0]);
            }
            for(int i = types.length - 1; i >= 0; i--){
                beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types[i]: beforeType}, null, types[i - 1]);
            }
        }
        return beforeType;
    }

}
