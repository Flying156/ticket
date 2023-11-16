package org.learn.index12306.biz.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.index12306.biz.orderservice.dao.entity.OrderDO;
import org.learn.index12306.biz.orderservice.dto.domain.OrderStatusReversalDTO;
import org.learn.index12306.biz.orderservice.dto.req.CancelTicketOrderReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderSelfPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailSelfRespDTO;
import org.learn.index12306.biz.orderservice.mq.event.PayResultCallbackOrderEvent;
import org.learn.index12306.framework.starter.convention.page.PageResponse;

/**
 * 订单服务接口
 *
 * @author Milk
 * @version 2023/10/21 19:35
 */
public interface OrderService extends IService<OrderDO> {

    /**
     * 跟据订单号查询车票订单
     *
     * @param orderSn 订单号
     * @return 订单详情
     */
    TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn);

    /**
     * 分页查询订单信息
     *
     * @param requestParam 跟据用户 ID 分页查询对象
     * @return 订单分页详情
     */
    PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam);

    /**
     * 分页查询自己的订单信息
     *
     * @param requestParam 跟据用户 ID 分页查询对象
     * @return 订单分页详情
     */
    PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam);

    /**
     * 创建订单
     *
     * @param requestParam 订单创建参数
     * @return 订单号
     */
    String createTicketOrder(TicketOrderCreateReqDTO requestParam);

    /**
     * 关闭订单
     *
     * @param requestParam 订单关闭参数
     * @return 是否成功关闭
     */
    boolean closeTicketOrder(CancelTicketOrderReqDTO requestParam);


    /**
     * 取消火车票订单
     *
     * @param requestParam 取消火车票订单入参
     */
    boolean cancelTickOrder(CancelTicketOrderReqDTO requestParam);

    /**
     * 订单状态反转
     *
     * @param requestParam 请求参数
     */
    void statusReversal(OrderStatusReversalDTO requestParam);

    /**
     * 支付结果回调订单
     *
     * @param requestParam 请求参数
     */
    void payCallbackOrder(PayResultCallbackOrderEvent requestParam);
}
