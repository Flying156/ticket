package org.learn.index12306.biz.payservice.dto.base;

/**
 * 支付回调请求入参
 *
 * @author Milk
 * @version 2023/11/10 22:40
 */
public interface PayCallbackRequest {

    /**
     * 获取阿里支付回调入参
     */
    AliPayCallbackRequest getAliPayCallBackRequest();


    /**
     * 构建查找支付回调策略实现类标识
     */
    String buildMark();

}
