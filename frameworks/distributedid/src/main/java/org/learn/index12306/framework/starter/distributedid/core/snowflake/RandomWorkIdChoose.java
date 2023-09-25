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

import org.springframework.beans.factory.InitializingBean;

/**
 * 使用随机数获取 WorkID
 *
 * @author Milk
 * @version 2023/9/23 11:26
 */
public class RandomWorkIdChoose extends AbstractWorkIdChooseTemplate  implements InitializingBean {

    /**
     * 由于 workID 和 dataID 各占 5bit, 各自十进制最大值为 31
     */
    @Override
    protected WorkIdWrapper chooseWorkId() {
        int start = 0, end = 31;
        return new WorkIdWrapper(getRandom(start, end), getRandom(start, end));
    }


    private static long getRandom(int start, int end){
        return (long)(Math.random() * (end - start + 1) + start);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        chooseAndInit();
    }
}
