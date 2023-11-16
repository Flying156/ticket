package org.learn.index12306.biz.payservice.dto;

import lombok.Data;
import org.learn.index12306.biz.payservice.dto.base.AbstractRefundRequest;

import java.math.BigDecimal;

/**
 * 退款请求命令
 *
 * @author Milk
 * @version 2023/11/15 10:27
 */
@Data
public class RefundCommand extends AbstractRefundRequest {

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 交易凭证号
     */
    private String tradeNo;

}
