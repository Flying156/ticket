package org.learn.index12306.biz.payservice.dto.base;

import lombok.Data;

/**
 * 抽象支付回调入参实体
 *
 * @author Milk
 * @version 2023/11/10 22:42
 */
@Data
public abstract class AbstractPayCallbackRequest implements PayCallbackRequest{

    private String orderRequestId;

    @Override
    public AliPayCallbackRequest getAliPayCallBackRequest() {
        return null;
    }

    @Override
    public String buildMark() {
        return null;
    }
}
