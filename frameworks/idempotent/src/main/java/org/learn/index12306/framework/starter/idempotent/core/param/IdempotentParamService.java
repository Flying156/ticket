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

package org.learn.index12306.framework.starter.idempotent.core.param;

import org.learn.index12306.framework.starter.idempotent.core.IdempotentExecuteHandler;

/**
 * 基于参数的幂等处理器实现接口
 *
 * @author Milk
 * @version 2023/10/6 12:12
 */
public interface IdempotentParamService extends IdempotentExecuteHandler {
}
