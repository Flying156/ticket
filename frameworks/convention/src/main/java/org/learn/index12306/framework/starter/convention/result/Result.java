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

package org.learn.index12306.framework.starter.convention.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 返回类，包装 code, message, data
 *
 * @author Milk
 * @version 2023/9/21 20:43
 */
@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {

    /**
     * serialVersionUID 用于确保在对象的序列化和反序列化过程中版本的一致性，它是处理对象版本控制的重要机制
     */
    @Serial
    private static final long serialVersionUID = 5679018624309023727L;

    /**
     * 成功的 code
     */
    public static final String SUCCESS_CODE = "0";

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回给前端的 code
     */
    private String code;


    /**
     * 返回给前端的消息
     */
    private String message;


    /**
     * 请求ID
     */
    private String requestId;

    public boolean isSuccess(){
        return SUCCESS_CODE.equals(code);
    }

}
