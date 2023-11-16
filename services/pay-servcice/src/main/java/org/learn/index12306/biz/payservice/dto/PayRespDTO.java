package org.learn.index12306.biz.payservice.dto;

import lombok.Data;

/**
 * 支付返回实体
 *
 * @author Milk
 * @version 2023/11/7 17:11
 */
@Data
public class PayRespDTO {

    /**
     * 调用支付返回信息
     */
    private String body;
}
