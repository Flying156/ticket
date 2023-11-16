package org.learn.index12306.biz.payservice.handler.base;

import org.learn.index12306.biz.payservice.dto.RefundResponse;
import org.learn.index12306.biz.payservice.dto.base.RefundRequest;

/**
 * 退款抽象类
 *
 * @author Milk
 * @version 2023/11/15 10:57
 */
public abstract class AbstractRefundHandler {

    public abstract RefundResponse refund(RefundRequest requestParam);

}
