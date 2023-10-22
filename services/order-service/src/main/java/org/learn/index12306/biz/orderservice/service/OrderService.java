package org.learn.index12306.biz.orderservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.index12306.biz.orderservice.dao.entity.OrderDO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderSelfPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailSelfRespDTO;
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
}
