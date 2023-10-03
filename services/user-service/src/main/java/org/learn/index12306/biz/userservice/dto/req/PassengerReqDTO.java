package org.learn.index12306.biz.userservice.dto.req;

import lombok.Data;

/**
 * 乘车人添加&修改 请求参数
 *
 * @author Milk
 * @version 2023/10/3 13:42
 */
@Data
public class PassengerReqDTO {

    /**
     * 乘车人 id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 证件号
     */
    private String idCard;


    /**
     * 优惠类型
     */
    private Integer discountType;

    /**
     * 手机号
     */
    private String phone;
}
