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

package org.learn.index12306.framework.starter.user.core;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.learn.index12306.framework.starter.bases.constant.UserConstant.*;

/**
 * 用户信息传输过滤器，拦截其中有用户信息的请求, 将其添加至上下文
 *
 * @author Milk
 * @version 2023/9/25 16:30
 */
public class UserTransmitFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String userId = httpServletRequest.getHeader(USER_ID_KEY);
        if(StringUtils.hasText(userId)){
            String username = httpServletRequest.getHeader(USER_NAME_KEY);
            String realName = httpServletRequest.getHeader(REAL_NAME_KEY);
            if(StringUtils.hasText(username)){
                username = URLDecoder.decode(username, StandardCharsets.UTF_8);
            }
            if(StringUtils.hasText(realName)){
                realName = URLDecoder.decode(realName, StandardCharsets.UTF_8);
            }
            String token = httpServletRequest.getHeader(USER_TOKEN_KEY);
            UserInfoDTO user = UserInfoDTO.builder()
                    .userId(userId)
                    .username(username)
                    .realName(realName)
                    .token(token)
                    .build();
            UserContext.setUser(user);
        }
        try{
            filterChain.doFilter(servletRequest, servletResponse);
        }finally {
            UserContext.removeUser();
        }
    }
}
