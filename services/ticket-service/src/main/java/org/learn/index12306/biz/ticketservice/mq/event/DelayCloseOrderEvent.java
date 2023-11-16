package org.learn.index12306.biz.ticketservice.mq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.domain.PurchaseTicketPassengerDetailDTO;
import org.learn.index12306.biz.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;

import java.util.List;

/**
 * 延迟关闭事件
 *
 * @author Milk
 * @version 2023/11/4 14:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public final class DelayCloseOrderEvent {

    /**
     * 列车 ID
     */
    private String trainId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 抵达站点
     */
    private String arrival;

    /**
     * 乘车购票信息
     */
    private List<TrainPurchaseTicketRespDTO> trainPurchaseTicketResults;

}
