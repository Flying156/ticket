package org.learn.index12306.biz.payservice.mq.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.biz.payservice.common.enums.RefundTypeEnum;
import org.learn.index12306.biz.payservice.remote.dto.TicketOrderPassengerDetailRespDTO;

import java.util.List;

/**
 * 退款回调事件
 *
 * @author Milk
 * @version 2023/11/15 13:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
