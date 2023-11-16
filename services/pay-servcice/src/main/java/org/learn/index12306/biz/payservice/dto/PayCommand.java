package org.learn.index12306.biz.payservice.dto;

/**
 * @author Milk
 * @version 2023/11/7 17:09
 */

import lombok.Data;
import org.learn.index12306.biz.payservice.dto.base.AbstractPayRequest;

import java.math.BigDecimal;

@Data
public final class PayCommand  extends AbstractPayRequest {

    /**
     * 子订单号
     */
    private String outOrderSn;

    /**
     * 订单总金额
     * 单位为元，精确到小数点后两位，取值范围：[0.01,100000000]
     */
    private BigDecimal totalAmount;

    /**
     * 订单标题
     * 注意：不可使用特殊字符，如 /，=，& 等
     */
    private String subject;

}
