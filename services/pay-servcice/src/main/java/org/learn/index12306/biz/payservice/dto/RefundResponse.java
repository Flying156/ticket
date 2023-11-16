package org.learn.index12306.biz.payservice.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 退款结果返回
 *
 * @author Milk
 * @version 2023/11/15 10:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public final class RefundResponse {

    /**
     * 退款状态
     */
    private Integer status;

    /**
     * 第三方交易凭证
     */
    private String tradeNo;

}
