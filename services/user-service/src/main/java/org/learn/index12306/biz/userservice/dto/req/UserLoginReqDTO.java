package org.learn.index12306.biz.userservice.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录请求参数
 *
 * @author Milk
 * @version 2023/10/2 20:14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginReqDTO {

    /**
     * 用户名
     */
    private String usernameOrMailOrPhone;

    /**
     * 密码
     */
    private String password;
}
