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

package org.learn.index12306.framework.starter.database.toolkit;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.convention.page.PageRequest;
import org.learn.index12306.framework.starter.convention.page.PageResponse;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页工具类
 * Mybatis Plus 的 Page 类和自定义 Page 转换
 *
 * @author Milk
 * @version 2023/9/22 21:12
 */
public class PageUtil {

    /**
     * {@link PageRequest} to {@link Page}
     */
    public static Page convert(PageRequest pageRequest){
        return convert(pageRequest.getCurrent(), pageRequest.getSize());
    }

    /**
     * {@link PageRequest} to {@link Page}
     */
    public static Page convert(long current, long size){
        return new Page(current, size);
    }

    /**
     * {@link IPage} build to {@link PageResponse}
     */
    public static PageResponse convert(IPage ipage){
        return buildConventionPage(ipage);
    }


    /**
     * {@link IPage} build to {@link PageResponse}
     */
    public static  <TARGET, ORIGIN> PageResponse<TARGET> convert(IPage<ORIGIN> ipage, Class<TARGET> clazz){
        ipage.convert(each -> BeanUtil.convert(each, clazz));
        return buildConventionPage(ipage);
    }

    /**
     * {@link IPage} build to {@link PageResponse}
     */
    public static <TARGET, ORIGIN> PageResponse<TARGET> convert(IPage<ORIGIN> ipage, Function<? super ORIGIN, ? extends TARGET> mapper){
        List<TARGET> targetList = ipage.getRecords().stream()
                .map(mapper)
                .collect(Collectors.toList());
        return PageResponse.<TARGET>builder()
                .total(ipage.getTotal())
                .current(ipage.getCurrent())
                .size(ipage.getSize())
                .records(targetList)
                .build();
    }

    /**
     * {@link IPage} build to {@link PageResponse}
     */
    public static PageResponse buildConventionPage(IPage iPage){
        return PageResponse.builder()
                .current(iPage.getCurrent())
                .size(iPage.getSize())
                .records(iPage.getRecords())
                .total(iPage.getTotal())
                .build();
    }


}
