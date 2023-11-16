package org.learn.index12306.biz.orderservice.dto.req;

import lombok.Data;

/**
 * 取消车票订单请求入参
 *
 * @author Milk
 * @version 2023/11/4 15:34
 */
@Data
public class CancelTicketOrderReqDTO {

    /**
     * 订单号
     */
    private String orderSn;
}
