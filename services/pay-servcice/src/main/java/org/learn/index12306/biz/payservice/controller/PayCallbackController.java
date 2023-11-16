package org.learn.index12306.biz.payservice.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.payservice.common.enums.PayChannelEnum;
import org.learn.index12306.biz.payservice.common.enums.TradeStatusEnum;
import org.learn.index12306.biz.payservice.convert.PayCallbackRequestConvert;
import org.learn.index12306.biz.payservice.dto.PayCallbackCommand;
import org.learn.index12306.biz.payservice.dto.base.PayCallbackRequest;
import org.learn.index12306.framework.starter.designpattern.strategy.AbstractStrategyChoose;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 支付结果回调
 *
 * @author Milk
 * @version 2023/11/10 22:16
 */
@RestController
@RequiredArgsConstructor
public class PayCallbackController {

    private final AbstractStrategyChoose abstractStrategyChoose;

    @PostMapping("/api/pay-service/callback/alipay")
    public void callbackAlipay(@RequestParam Map<String, Object> requestParam){
        PayCallbackCommand payCallbackCommand = BeanUtil.mapToBean(requestParam, PayCallbackCommand.class,true, CopyOptions.create());
        payCallbackCommand.setChannel(PayChannelEnum.ALI_PAY.getCode());
        payCallbackCommand.setTradeStatus(TradeStatusEnum.TRADE_SUCCESS.tradeCode().toString());
        payCallbackCommand.setOrderRequestId(requestParam.get("out_trade_no").toString());
        payCallbackCommand.setGmtPayment(DateUtil.parse(requestParam.get("gmt_payment").toString()));

        PayCallbackRequest payCallbackRequest = PayCallbackRequestConvert.convert(payCallbackCommand);
        abstractStrategyChoose.chooseAndExecute(payCallbackRequest.buildMark(), payCallbackRequest);

    }

}
