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

package org.learn.index12306.framework.starter.convention.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 自定义分页查询返回类，方便更换框架时迁移
 *
 * @author Milk
 * @version 2023/9/21 21:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> implements Serializable {

    private static Long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    private Long current;

    /**
     * 当前页面大小
     */
    private Long size = 10L;

    /**
     * 数据的总数
     */
    private Long total;

    /**
     * 记录数据
     */
    private List<T> records = Collections.emptyList();

    public PageResponse(long current, long size){
        this(current, size, 0L);
    }

    public PageResponse(long current, long size, long total){
        if(current > 1){
            this.current = current;
        }
        this.size = size;
        this.total = total;
    }

    public PageResponse setRecord(List<T> records){
        this.records = records;
        return this;
    }

    /**
     * 转换类中的类型
     */
    public <S> PageResponse<S>  convert(Function<? super T, ? extends S> mapper){
        List<S> convertList = this.getRecords().stream().map(mapper).collect(Collectors.toList());
        return ((PageResponse<S>) this).setRecord(convertList);
    }

}
