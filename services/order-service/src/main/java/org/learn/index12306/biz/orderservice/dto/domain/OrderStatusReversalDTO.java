package org.learn.index12306.biz.orderservice.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemDO;

import java.util.List;

/**
 * 订单状态反转实体
 *
 * @author Milk
 * @version 2023/11/12 22:08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusReversalDTO {

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 订单反转后状态
     */
    private Integer orderStatus;

    /**
     * 订单明细反转后状态
     */
    private Integer orderItemStatus;


}
