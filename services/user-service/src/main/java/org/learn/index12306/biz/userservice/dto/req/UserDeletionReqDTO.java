package org.learn.index12306.biz.userservice.dto.req;

import lombok.Data;

/**
 * 用户注销请求参数
 *
 * @author Milk
 * @version 2023/10/2 17:33
 */
@Data
public class UserDeletionReqDTO {

    /**
     * 用户名
     */
    private String username;

}
