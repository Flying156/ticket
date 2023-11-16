package org.learn.index12306.biz.orderservice.mq.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.biz.orderservice.common.enums.RefundTypeEnum;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;

import java.util.List;

/**
 * 退款结果回调事件
 *
 * @author Milk
 * @version 2023/11/16 10:35
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundResultCallbackOrderEvent {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 退款类型
     */
    private RefundTypeEnum refundTypeEnum;

    /**
     * 退款的车票列表
     */
    private List<TicketOrderPassengerDetailRespDTO> partialRefundTicketDetailList;

}
