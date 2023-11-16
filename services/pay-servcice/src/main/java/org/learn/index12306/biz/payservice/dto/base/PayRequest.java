package org.learn.index12306.biz.payservice.dto.base;

import java.math.BigDecimal;

/**
 * 支付入参接口
 *
 * @author Milk
 * @version 2023/11/7 16:43
 */
public interface PayRequest {

    /**
     * 获取阿里支付入参
     */
    AliPayRequest getAliPayRequest();

    /**
     * 获取订单号
     */
    String getOrderSn();

    /**
     * 商户订单号
     */
    String getOrderRequestId();

    /**
     * 获取总金额
     */
    BigDecimal getTotalAmount();

    /**
     * 构建查找支付策略实现类标识
     */
    String buildMark();



}
