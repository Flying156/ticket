package org.learn.index12306.biz.orderservice.dto.req;

import lombok.Data;

import java.util.List;

/**
 * 车票子订单详情
 *
 * @author Milk
 * @version 2023/10/22 10:30
 */
@Data
public class TicketOrderItemQueryReqDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 子订单记录id
     */
    private List<Long> orderItemRecordIds;

}
