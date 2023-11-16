package org.learn.index12306.biz.payservice.handler.base;

import org.learn.index12306.biz.payservice.dto.base.PayCallbackRequest;

/**
 * 抽象回调组件
 *
 * @author Milk
 * @version 2023/11/11 13:59
 */
public abstract class AbstractPayCallbackHandler {

    /**
     * 支付回调方法
     *
     * @param payCallbackRequest 支付回调请求
     */
    public abstract void callback(PayCallbackRequest payCallbackRequest);

}
