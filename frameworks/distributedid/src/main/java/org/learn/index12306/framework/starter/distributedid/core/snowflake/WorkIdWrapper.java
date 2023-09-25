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

package org.learn.index12306.framework.starter.distributedid.core.snowflake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WorkId 包装器
 * <p>
 *     雪花算法的组成 时间戳 + 工作机器 ID + 序列号
 *     工作机器 ID 共 10bit,高位 5bit 是数据中心 ID, 低位 5bit 是工作节点 ID
 * </p>
 *
 * @author Milk
 * @version 2023/9/23 10:56
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkIdWrapper {

    /**
     * 工作节点 ID
     */
    private Long workId;

    /**
     * 数据中心 ID
     */
    private Long dataCenterId;
}
