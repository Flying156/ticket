package org.learn.index12306.biz.payservice.dto;

import lombok.Data;
import org.learn.index12306.biz.payservice.remote.dto.TicketOrderPassengerDetailRespDTO;

import java.util.List;

/**
 * 退款创建实体类
 *
 * @author Milk
 * @version 2023/11/13 22:05
 */
@Data
public class RefundCreateDTO {

    /**
     * 支付流水号
     */
    private String paySn;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 退款类型
     */
    private Integer type;

    /**
     * 部分退款车票详情集合
     */
    private List<TicketOrderPassengerDetailRespDTO> refundDetailReqDTOList;

}
