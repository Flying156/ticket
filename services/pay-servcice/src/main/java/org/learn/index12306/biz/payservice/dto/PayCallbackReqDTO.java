package org.learn.index12306.biz.payservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 支付单回调请求参数
 *
 * @author Milk
 * @version 2023/11/11 14:13
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayCallbackReqDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 商户订单号
     */
    private String outOrderSn;

    /**
     * 支付渠道
     */
    private String channel;

    /**
     * 支付环境
     */
    private Integer tradeType;

    /**
     * 交易凭证号
     */
    private String tradeNo;

    /**
     * 订单标题
     */
    private String subject;

    /**
     * 交易总金额
     */
    private Integer totalAmount;

    /**
     * 付款时间
     */
    private Date gmtPayment;

    /**
     * 支付金额
     */
    private Integer payAmount;

    /**
     * 支付状态
     */
    private Integer status;

    /**
     * 商户订单号
     */
    private String orderRequestId;

}
