package org.learn.index12306.biz.userservice.dto.req;

import lombok.Data;

/**
 * 用户请求删除乘客参数
 *
 * @author Milk
 * @version 2023/10/3 15:03
 */
@Data
public class PassengerRemoveReqDTO {

    /**
     * 乘车人 ID
     */
    private String id;
}
