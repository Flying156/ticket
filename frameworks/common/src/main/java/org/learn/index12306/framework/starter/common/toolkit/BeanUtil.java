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

package org.learn.index12306.framework.starter.common.toolkit;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import com.github.dozermapper.core.loader.api.BeanMappingBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.*;

import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapEmptyString;
import static com.github.dozermapper.core.loader.api.TypeMappingOptions.mapNull;

/**
 * 对象属性复制工具
 *
 * @author Milk
 * @version 2023/9/21 10:47
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BeanUtil {

    protected static Mapper BEAN_MAPPER_BUILDER;

    static{
        BEAN_MAPPER_BUILDER = DozerBeanMapperBuilder.buildDefault();
    }

    /**
     * 属性复制
     *
     * @param source 数据对象
     * @param target 目标对象
     * @return       目标对象
     */
    public static <T, S> T convert(S source, T target){
        Optional.ofNullable(source)
                .ifPresent(each -> BEAN_MAPPER_BUILDER.map(each, target));
        return target;
    }

    /**
     * 根据给定的 Class 复制属性
     *
     * @param source 数据对象
     * @param clazz  目标对象 Class
     * @return       目标对象
     */
    public static <T, S> T convert(S source, Class<T> clazz){
        return Optional.ofNullable(source)
                .map(each -> BEAN_MAPPER_BUILDER.map(source, clazz))
                .orElse(null);
    }

    /**
     * 复制多个对象
     * @param source 数据对象
     * @param clazz  目标对像
     * @return       装换后的对象集合
     */
    public static <T, S> List<T> convert(List<S> source, Class<T>clazz){
        return Optional.ofNullable(source)
                .map(each ->{
                    List<T> targetList = new ArrayList<T>(each.size());
                    each.forEach(item -> targetList.add(BEAN_MAPPER_BUILDER.map(item,clazz)));
                    return targetList;
                }).orElse(null);
    }

    /**
     * 复制多个对象
     * @param source 数据对象
     * @param clazz  目标对像
     * @return       装换后的对象集合
     */
    public static <T, S> Set<T> convert(Set<S> source, Class<T> clazz){
        return Optional.ofNullable(source)
                .map(each ->{
                    Set<T> targetSet = new HashSet<T>(each.size());
                    each.forEach(item -> targetSet.add(BEAN_MAPPER_BUILDER.map(item, clazz)));
                    return targetSet;
                }).orElse(null);
    }

    /**
     * 复制多个对象
     * @param source 数据对象
     * @param clazz  目标对像
     * @return       装换后的对象集合
     */
    public static <T, S> T[] convert(S[] source, Class<T> clazz){
        return Optional.ofNullable(source)
                .map(each ->{
                    @SuppressWarnings("unchecked")
                    T[] targetArray = (T[]) Array.newInstance(clazz, source.length);
                    for(int i = 0; i < targetArray.length; i++){
                        targetArray[i] = BEAN_MAPPER_BUILDER.map(source[i], clazz);
                    }
                    return targetArray;
                }).orElse(null);
    }

    /**
     * 复制非空且非 NULL 的字符串
     *
     * @param source 被拷贝的对象
     * @param target 目标对象
     */
    public static void convertIgnoreNullAndBlank(Object source, Object target){
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilder(new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(source.getClass(), target.getClass(), mapNull(false), mapEmptyString(false));
            }
        }).build();
        mapper.map(source,target);
    }

    /**
     * 复制非 NULL 的字符串
     *
     * @param source 被拷贝的对象
     * @param target 目标对象
     */
    public static void convertIgnoreNull(Object source, Object target){
        DozerBeanMapperBuilder dozerBeanMapperBuilder = DozerBeanMapperBuilder.create();
        Mapper mapper = dozerBeanMapperBuilder.withMappingBuilder(new BeanMappingBuilder() {
                  @Override
                  protected void configure() {
                      mapping(source.getClass(), target.getClass(),mapNull(false));
                  }
              }
        ).build();
        mapper.map(source, target);
    }

}
