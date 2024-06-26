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

package org.learn.index12306.framework.starter.bases;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 单例对象容器
 * <p>
 *     单例模式是设计模式之一，一个类创创建自己的唯一对象
 *      1、单例类只能有一个实例。
 *      2、单例类必须自己创建自己的唯一实例。
 *      3、单例类必须给所有其他对象提供这一实例。
 * </p>
 * @author Milk
 * @version 2023/9/21 18:53
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Singleton {

    public static final ConcurrentHashMap<String, Object> SINGLE_OBJECT_POOL = new ConcurrentHashMap<>();


    /**
     * 获取单例对象
     */
    public static<T> T get(String key){
        Object result = SINGLE_OBJECT_POOL.get(key);
        return result == null ? null : (T)result;
    }

    /**
     * 获取对象，如果对象不存在且 supplier 不为空，构建对象并放入容器
     */
    public static<T> T get(String key, Supplier<T> supplier) {
        Object result = SINGLE_OBJECT_POOL.get(key);
        if (result == null && (result = supplier.get()) != null) {
            SINGLE_OBJECT_POOL.put(key, result);
        }
        return result != null ?  (T) result : null;
    }


    /**
     * 对象放入容器
     */
    public static void put(Object value){
        put(value.getClass().getName(), value);
    }

    /**
     * 对象放入容器
     */
    public static void put(String key, Object value){
        SINGLE_OBJECT_POOL.put(key, value);
    }
}
