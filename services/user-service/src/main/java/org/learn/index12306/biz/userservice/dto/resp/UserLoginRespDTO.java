package org.learn.index12306.biz.userservice.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登陆后返回的数据
 *
 * @author Milk
 * @version 2023/10/2 20:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRespDTO {


    /**
     * 用户 ID
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * Token
     */
    private String accessToken;

}
