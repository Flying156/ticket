package org.learn.index12306.biz.payservice.handler.base;

import org.learn.index12306.biz.payservice.dto.PayResponse;
import org.learn.index12306.biz.payservice.dto.base.PayRequest;

/**
 * 抽象支付组件
 *
 * @author Milk
 * @version 2023/11/7 22:26
 */
public abstract class AbstractPayHandler {

    /**
     * 支付抽象接口
     *
     * @param payRequest 支付请求参数
     * @return 支付响应参数
     */
    public abstract PayResponse pay(PayRequest payRequest);

}
