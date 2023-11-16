package org.learn.index12306.biz.payservice.convert;

import org.learn.index12306.biz.payservice.common.enums.PayChannelEnum;
import org.learn.index12306.biz.payservice.dto.PayCommand;
import org.learn.index12306.biz.payservice.dto.base.AliPayRequest;
import org.learn.index12306.biz.payservice.dto.base.PayRequest;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;

import java.util.Objects;

/**
 * 支付请求入参转换器
 *
 * @author Milk
 * @version 2023/11/7 18:33
 */
public final class PayRequestConvert {

    public static PayRequest command2PayRequest(PayCommand payCommand){
        PayRequest payRequest = null;
        if(Objects.equals(payCommand.getChannel(), PayChannelEnum.ALI_PAY.getCode())){
            payRequest = BeanUtil.convert(payCommand, AliPayRequest.class);
        }
        return payRequest;
    }

}
