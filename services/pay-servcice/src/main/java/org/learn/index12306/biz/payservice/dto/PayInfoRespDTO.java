package org.learn.index12306.biz.payservice.dto;

import lombok.Data;

import java.util.Date;

/**
 * 支付详情信息返回参数
 *
 * @author Milk
 * @version 2023/11/9 21:40
 */
@Data
public class PayInfoRespDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 总金额
     */
    private Integer totalAmount;


    /**
     * 支付状态
     */
    private Integer status;

    /**
     * 支付时间
     */
    private Date gmtPayment;

}
