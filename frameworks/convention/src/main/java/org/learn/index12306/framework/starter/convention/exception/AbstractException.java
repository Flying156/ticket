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

package org.learn.index12306.framework.starter.convention.exception;

import lombok.Getter;
import org.learn.index12306.framework.starter.convention.errorCode.IErrorCode;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * 抽象底层 Exception
 *
 * @author Milk
 * @version 2023/9/21 21:42
 */
@Getter
public abstract class AbstractException extends RuntimeException{

    /**
     * 返回 CODE
     */
    public final String errorCode;

    /**
     * 错误信息
     */
    public final String errorMessage;


    public AbstractException(String message,Throwable throwable, IErrorCode errorCode){
        super(message, throwable);
        this.errorCode = errorCode.code();
        this.errorMessage = Optional.ofNullable(StringUtils.hasLength(message) ? message: null).orElse(errorCode.message());
    }


}
