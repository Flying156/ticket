package org.learn.index12306.biz.orderservice.mq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemCreateReqDTO;

import java.util.List;

/**
 * 延迟关闭订单事件
 *
 * @author Milk
 * @version 2023/10/23 9:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DelayCloseOrderEvent {

    /**
     * 车次 ID
     */
    private String trainId;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 乘车人购票信息
     */
    private List<TicketOrderItemCreateReqDTO>trainPurchaseTicketResults;
}
