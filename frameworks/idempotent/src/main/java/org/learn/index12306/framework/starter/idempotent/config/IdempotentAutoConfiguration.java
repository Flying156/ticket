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

package org.learn.index12306.framework.starter.idempotent.config;

import org.learn.index12306.framework.starter.idempotent.core.IdempotentAspect;
import org.learn.index12306.framework.starter.idempotent.core.param.IdempotentParamExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.param.IdempotentParamService;
import org.learn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELService;
import org.learn.index12306.framework.starter.idempotent.core.token.IdempotentTokenController;
import org.learn.index12306.framework.starter.idempotent.core.token.IdempotentTokenExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.token.IdempotentTokenService;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 幂等自动装配
 *
 * @author Milk
 * @version 2023/10/5 19:51
 */
@EnableConfigurationProperties(IdempotentProperties.class)
public class IdempotentAutoConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(){
        return new IdempotentAspect();
    }


    /**
     * Param 验证方式幂等，基于 RestAPI 场景，本质上是分布式锁
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentParamService idempotentParamService(RedissonClient redissonClient){
        return new IdempotentParamExecuteHandler(redissonClient);
    }

    /**
     * Token 方式幂等实现，基于 RestAPI 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentTokenService idempotentTokenExecuteHandler(DistributedCache distributedCache, IdempotentProperties idempotentProperties){
        return new IdempotentTokenExecuteHandler(distributedCache, idempotentProperties);
    }

    /**
     * 申请幂等 Token 控制器，基于 RestAPI 场景
     */
    @Bean
    public IdempotentTokenController idempotentTokenController(IdempotentTokenService idempotentTokenService) {
        return new IdempotentTokenController(idempotentTokenService);
    }

    /**
     * SpEL 方式幂等实现，基于 RestAPI 场景，本质上是分布式锁
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELService idempotentSpELByRestAPIExecuteHandler(RedissonClient redissonClient) {
        return new IdempotentSpELByRestAPIExecuteHandler(redissonClient);
    }

    /**
     * SpEL 方式幂等实现，基于 MQ 场景
     */
    @Bean
    @ConditionalOnMissingBean
    public IdempotentSpELByMQExecuteHandler idempotentSpELByMQExecuteHandler(DistributedCache distributedCache) {
        return new IdempotentSpELByMQExecuteHandler(distributedCache);
    }

}
