package org.learn.index12306.biz.payservice.convert;

import org.learn.index12306.biz.payservice.common.enums.PayChannelEnum;
import org.learn.index12306.biz.payservice.dto.PayCallbackCommand;
import org.learn.index12306.biz.payservice.dto.base.PayCallbackRequest;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;

import java.util.Objects;

/**
 * @author Milk
 * @version 2023/11/10 23:15
 */
public class PayCallbackRequestConvert {

    public static PayCallbackRequest convert(PayCallbackCommand payCallbackCommand){
        PayCallbackRequest payCallbackRequest = null;
        if(Objects.equals(payCallbackCommand.getChannel(), PayChannelEnum.ALI_PAY.getCode())){
            payCallbackRequest = BeanUtil.convert(payCallbackCommand.getAliPayRequest(), PayCallbackRequest.class);
        }
        return payCallbackRequest;
    }

}
