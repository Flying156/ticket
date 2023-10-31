package org.learn.index12306.biz.orderservice.controller;

import cn.crane4j.annotation.AutoOperate;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderSelfPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailSelfRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;
import org.learn.index12306.biz.orderservice.service.OrderItemService;
import org.learn.index12306.biz.orderservice.service.OrderService;
import org.learn.index12306.framework.starter.convention.page.PageResponse;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车票订单接口控制层
 *
 * @author Milk
 * @version 2023/10/21 19:58
 */
@RestController
@RequiredArgsConstructor
public class TicketOrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;



    /**
     * 根据订单号查询车票订单
     */
    @GetMapping("/api/order-service/order/ticket/query")
    public Result<TicketOrderDetailRespDTO>queryTicketOrderByOrderSn(@RequestParam(value= "orderSn")String orderSn){
        return Results.success(orderService.queryTicketOrderByOrderSn(orderSn));
    }

    /**
     * 根据子订单记录 id 查询车票子订单详情
     */
    @GetMapping("/api/order-service/order/item/ticket/query")
    public Result<List<TicketOrderPassengerDetailRespDTO>>queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam){
        return Results.success(orderItemService.queryTicketItemOrderById(requestParam));
    }

    /**
     * 分页查询车票订单
     */
    @AutoOperate(type = TicketOrderDetailRespDTO.class, on = "data.records")
    @GetMapping("/api/order-service/order/ticket/page")
    public Result<PageResponse<TicketOrderDetailRespDTO>> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam){
        return Results.success(orderService.pageTicketOrder(requestParam));
    }

    /**
     * 分页查询自己的车票订单
     */
    @GetMapping("/api/order-service/order/ticket/self/page")
    public Result<PageResponse<TicketOrderDetailSelfRespDTO>> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam){
        return Results.success(orderService.pageSelfTicketOrder(requestParam));
    }

    /**
     * 创建车票订单
     */
    @PostMapping("/api/order-service/order/ticket/create")
    public Result<String> createTicketOrder(@RequestBody TicketOrderCreateReqDTO requestParam){
        return Results.success(orderService.createTicketOrder(requestParam));
    }
}
