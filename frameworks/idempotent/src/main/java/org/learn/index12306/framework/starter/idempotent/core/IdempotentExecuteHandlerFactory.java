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

package org.learn.index12306.framework.starter.idempotent.core;

import org.learn.index12306.framework.starter.bases.ApplicationContextHolder;
import org.learn.index12306.framework.starter.idempotent.core.param.IdempotentParamService;
import org.learn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELByMQExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.spel.IdempotentSpELByRestAPIExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.token.IdempotentTokenService;
import org.learn.index12306.framework.starter.idempotent.enums.IdempotentSceneEnum;
import org.learn.index12306.framework.starter.idempotent.enums.IdempotentTypeEnum;

/**
 * 幂等执行处理器工厂
 * <p>
 *     使用简单工厂模式，以控制反转的思想来控制不同的幂等执行处理器
 * </p>
 *
 * @author Milk
 * @version 2023/10/5 19:03
 */
public final class IdempotentExecuteHandlerFactory {

    /**
     * 获得幂等处理器
     *
     * @param scene 幂等的应用场景
     * @param type 幂等的实现方式
     * @return 对应的幂等处理器
     */
    public static IdempotentExecuteHandler getInstance(IdempotentSceneEnum scene, IdempotentTypeEnum type){
        IdempotentExecuteHandler result = null;
        switch(scene){
            case RESTAPI -> {
                switch(type){
                    case PARAM -> result = ApplicationContextHolder.getBean(IdempotentParamService.class);
                    case TOKEN -> result = ApplicationContextHolder.getBean(IdempotentTokenService.class);
                    case SPEL -> result = ApplicationContextHolder.getBean(IdempotentSpELByRestAPIExecuteHandler.class);
                    default -> {}
                }
            }
            case MQ -> result = ApplicationContextHolder.getBean(IdempotentSpELByMQExecuteHandler.class);
            default -> {}
        }
        return result;
    }

}
