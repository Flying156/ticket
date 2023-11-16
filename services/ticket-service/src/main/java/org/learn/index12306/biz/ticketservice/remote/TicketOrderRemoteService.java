package org.learn.index12306.biz.ticketservice.remote;

import org.learn.index12306.biz.ticketservice.remote.dto.CancelTicketOrderReqDTO;
import org.learn.index12306.biz.ticketservice.remote.dto.TicketOrderCreateRemoteReqDTO;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 车票订单远程调用
 *
 * @author Milk
 * @version 2023/10/24 23:09
 */
@FeignClient(value = "index12306-order${unique-name:}-service")
public interface TicketOrderRemoteService {


    /**
     * 远程创建订单调用服务
     */
    @PostMapping("/api/order-service/order/ticket/create")
    Result<String> createTicketOrder(@RequestBody TicketOrderCreateRemoteReqDTO requestParam);

    /**
     * 车票订单关闭
     *
     * @param requestParam 车票订单关闭入参
     * @return 关闭订单返回结果
     */
    @PostMapping("/api/order-service/order/ticket/close")
    Result<Boolean> closeTickOrder(@RequestBody CancelTicketOrderReqDTO requestParam);

    /**
     * 车票订单取消
     *
     * @param requestParam 车票订单取消入参
     * @return 订单取消返回结果
     */
    @PostMapping("/api/order-service/order/ticket/cancel")
    Result<Void> cancelTicketOrder(@RequestBody CancelTicketOrderReqDTO requestParam);
}
