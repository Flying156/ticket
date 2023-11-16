package org.learn.index12306.biz.payservice.dto.base;

/**
 * 退款入参接口
 *
 * @author Milk
 * @version 2023/11/15 10:27
 */
public interface RefundRequest {

    AliRefundRequest getAliRefundRequest();

    /**
     * 获取订单号
     */
    String getOrderSn();

    /**
     * 构建查找支付策略实现类标识
     */
    String buildMark();

}
