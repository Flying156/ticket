package org.learn.index12306.biz.userservice.dto.resp;

import lombok.Data;

/**
 * 用户注册返回参数
 *
 * @author Milk
 * @version 2023/10/1 21:27
 */
@Data
public class UserRegisterRespDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

}
