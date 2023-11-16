package org.learn.index12306.biz.ticketservice.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单取消
 *
 * @author Milk
 * @version 2023/11/4 17:05
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelTicketOrderReqDTO {

    /**
     * 订单号
     */
    private String orderSn;

}
