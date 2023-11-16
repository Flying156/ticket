package org.learn.index12306.biz.payservice.handler;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.payservice.common.enums.PayChannelEnum;
import org.learn.index12306.biz.payservice.common.enums.TradeStatusEnum;
import org.learn.index12306.biz.payservice.dao.entity.PayDO;
import org.learn.index12306.biz.payservice.dto.PayCallbackReqDTO;
import org.learn.index12306.biz.payservice.dto.base.AliPayCallbackRequest;
import org.learn.index12306.biz.payservice.dto.base.PayCallbackRequest;
import org.learn.index12306.biz.payservice.handler.base.AbstractPayCallbackHandler;
import org.learn.index12306.biz.payservice.service.PayService;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.designpattern.strategy.AbstractExecuteStrategy;
import org.springframework.stereotype.Component;

/**
 * 支付回调实现
 *
 * @author Milk
 * @version 2023/11/11 14:04
 */
@Component
@RequiredArgsConstructor
public class AliPayCallbackHandler extends AbstractPayCallbackHandler implements AbstractExecuteStrategy<PayCallbackRequest, Void> {

    private final PayService payService;

    @Override
    public void callback(PayCallbackRequest payCallbackRequest) {
        AliPayCallbackRequest aliPayCallbackRequest = payCallbackRequest.getAliPayCallBackRequest();
        PayCallbackReqDTO payCallbackReqDTO = PayCallbackReqDTO.builder()
                .payAmount(aliPayCallbackRequest.getBuyerPayAmount())
                .orderSn(aliPayCallbackRequest.getOrderRequestId())
                .tradeNo(aliPayCallbackRequest.getTradeNo())
                .status(TradeStatusEnum.queryActualTradeStatusCode(aliPayCallbackRequest.getTradeStatus()))
                .gmtPayment(aliPayCallbackRequest.getGmtPayment())
                .build();
        payService.callbackPay(payCallbackReqDTO);
    }

    @Override
    public String mark() {
        return PayChannelEnum.ALI_PAY.getName();
    }

    @Override
    public void execute(PayCallbackRequest requestParam) {
        callback(requestParam);
    }
}
