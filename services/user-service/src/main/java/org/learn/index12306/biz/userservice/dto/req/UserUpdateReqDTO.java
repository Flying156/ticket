package org.learn.index12306.biz.userservice.dto.req;

import lombok.Data;

/**
 * 用户修改请求参数
 *
 * @author Milk
 * @version 2023/10/2 16:18
 */
@Data
public class UserUpdateReqDTO {

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 旅客类型
     */
    private Integer userType;

    /**
     * 邮编
     */
    private String postCode;

    /**
     * 地址
     */
    private String address;
}
