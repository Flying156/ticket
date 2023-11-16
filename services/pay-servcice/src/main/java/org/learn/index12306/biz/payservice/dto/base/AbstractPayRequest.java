package org.learn.index12306.biz.payservice.dto.base;

import lombok.Data;
import org.learn.index12306.framework.starter.distributedid.toolkit.SnowflakeIdUtil;

/**
 * 抽象支付入参实体
 *
 * @author Milk
 * @version 2023/11/7 16:42
 */
@Data
public abstract class AbstractPayRequest implements PayRequest{

    /**
     * 交易环境，H5、小程序、网站等
     */
    private Integer tradeType;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 支付渠道
     */
    private Integer channel;

    /**
     * 商户订单号
     * 由商家自定义，64个字符以内，仅支持字母、数字、下划线且需保证在商户端不重复
     */
    private String orderRequestId = SnowflakeIdUtil.nextIdStr();

    @Override
    public AliPayRequest getAliPayRequest() {
        return null;
    }

    @Override
    public String getOrderRequestId() {
        return orderRequestId;
    }

    @Override
    public String buildMark() {
        return null;
    }
}
