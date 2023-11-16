package org.learn.index12306.biz.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemDO;
import org.learn.index12306.biz.orderservice.dto.domain.OrderItemStatusReversalDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;

import java.util.List;

/**
 * 订单详情服务接口
 *
 * @author Milk
 * @version 2023/10/21 19:45
 */
public interface OrderItemService extends IService<OrderItemDO> {

    /**
     * 根据子订单记录id查询车票子订单详情
     *
     * @param requestParam 请求参数
     */
    List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam);

    /**
     * 退款后更新订单状态
     *
     * @param requestParam 订单信息
     */
    void orderItemStatusReversal(OrderItemStatusReversalDTO requestParam);
}
