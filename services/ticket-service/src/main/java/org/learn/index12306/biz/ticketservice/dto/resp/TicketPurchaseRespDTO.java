package org.learn.index12306.biz.ticketservice.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 车票购买返回参数
 *
 * @author Milk
 * @version 2023/10/12 19:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TicketPurchaseRespDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 乘车人订单详情
     */
    private List<TicketOrderDetailRespDTO> ticketOrderDetails;
}
