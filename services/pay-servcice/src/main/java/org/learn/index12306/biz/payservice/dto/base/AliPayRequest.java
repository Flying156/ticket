package org.learn.index12306.biz.payservice.dto.base;

import lombok.Data;
import lombok.experimental.Accessors;
import org.learn.index12306.biz.payservice.common.enums.PayChannelEnum;
import org.learn.index12306.biz.payservice.common.enums.PayTradeTypeEnum;

import java.math.BigDecimal;

/**
 * 支付宝支付请求入参
 *
 * @author Milk
 * @version 2023/11/7 16:51
 */
@Data
@Accessors(chain = true)
public final class AliPayRequest extends AbstractPayRequest{

    /**
     * 商户订单号
     * 由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复
     */
    private String outOrderSn;

    /**
     * 订单总金额
     * 单位为元，精确到小数点后两位
     */
    private BigDecimal totalAmount;

    /**
     * 订单标题
     * 注意：不可使用特殊字符，如 /，=，& 等
     */
    private String subject;

    /**
     * 交易凭证号
     */
    private String tradeNo;

    @Override
    public AliPayRequest getAliPayRequest() {
        return this;
    }

    @Override
    public String buildMark() {
        String mark = PayChannelEnum.ALI_PAY.name();
        if (getTradeType() != null) {
            mark = PayChannelEnum.ALI_PAY.name() + "_" + PayTradeTypeEnum.findNameByCode(getTradeType());
        }
        return mark;
    }

}
