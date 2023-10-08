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

package org.learn.index12306.framework.starter.web;

import org.learn.index12306.framework.starter.convention.errorCode.BaseErrorCode;
import org.learn.index12306.framework.starter.convention.exception.AbstractException;
import org.learn.index12306.framework.starter.convention.result.Result;

import java.util.Optional;

/**
 * 统一返回类
 *
 * @author Milk
 * @version 2023/9/24 15:54
 */
public final class Results {

    /**
     * 不返回数据，响应成功
     */
    public static Result<Void> success(){
        return new Result<Void>().setCode(Result.SUCCESS_CODE);
    }

    /**
     * 返回数据的成功响应
     */
    public static <T> Result<T> success(T data){
        return new Result<T>()
                .setData(data)
                .setCode(Result.SUCCESS_CODE);
    }

    /**
     * 返回服务端失败响应
     */
    public static Result<Void> failure(){
        return new Result<Void>()
                .setCode(BaseErrorCode.SERVICE_ERROR.code())
                .setMessage(BaseErrorCode.SERVICE_ERROR.message());
    }

    /**
     * 通过 errorCode、errorMessage 构建失败响应
     */
    public static Result<Void> failure(String errorCode, String errorMessage){
        return new Result<Void>()
                .setMessage(errorMessage)
                .setCode(errorCode);
    }

    /**
     * 通过 {@link AbstractException} 构建失败响应
     */
    public static Result<Void> failure(AbstractException abstractException){
        String errorCode = Optional.ofNullable(abstractException.getErrorCode())
                .orElse(BaseErrorCode.SERVICE_ERROR.code());
        String errorMessage = Optional.ofNullable(abstractException.getErrorMessage())
                .orElse(BaseErrorCode.SERVICE_ERROR.message());
        return new Result<Void>()
                .setCode(errorCode)
                .setMessage(errorMessage);
    }
}
