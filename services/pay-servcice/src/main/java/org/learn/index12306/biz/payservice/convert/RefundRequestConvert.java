package org.learn.index12306.biz.payservice.convert;

import org.learn.index12306.biz.payservice.common.enums.PayChannelEnum;
import org.learn.index12306.biz.payservice.dto.RefundCommand;
import org.learn.index12306.biz.payservice.dto.base.RefundRequest;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;

import java.util.Objects;

/**
 * @author Milk
 * @version 2023/11/15 10:54
 */
public class RefundRequestConvert {


    public static RefundRequest command2RefundRequest(RefundCommand refundCommand){
        RefundRequest refundRequest = null;
        if(Objects.equals(refundCommand.getChannel(), PayChannelEnum.ALI_PAY.getCode())){
            refundRequest = BeanUtil.convert(refundRequest, RefundRequest.class);
        }
        return refundRequest;
    }

}
