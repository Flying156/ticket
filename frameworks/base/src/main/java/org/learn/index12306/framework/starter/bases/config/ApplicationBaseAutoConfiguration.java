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

package org.learn.index12306.framework.starter.bases.config;

import org.learn.index12306.framework.starter.bases.ApplicationContextHolder;
import org.learn.index12306.framework.starter.bases.init.ApplicationContentPostProcessor;
import org.learn.index12306.framework.starter.bases.safa.FastJsonSafeMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean 自动装配, 自定义 Starter
 *
 * @author Milk
 * @version 2023/9/21 17:33
 */
public class ApplicationBaseAutoConfiguration {

    /**
     * 根据情况扫描包内的所有 Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContentPostProcessor applicationContentPostProcessor(ApplicationContext applicationContext){
        return new ApplicationContentPostProcessor(applicationContext);
    }


    /**
     * 根据属性是否开启 FastJson 安全模式
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.fastjson.safa-mode", havingValue = "true")
    public FastJsonSafeMode fastJsonSafeMode(){
        return new FastJsonSafeMode();
    }

}
