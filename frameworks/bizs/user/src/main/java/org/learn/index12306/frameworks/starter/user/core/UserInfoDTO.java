package org.learn.index12306.frameworks.starter.user.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户信息实体
 *
 * @author Milk
 * @version 2023/9/25 15:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInfoDTO {

    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户真实姓名
     */
    private String realName;

    /**
     * 用户 token
     */
    private String token;

}
