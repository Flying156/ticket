package org.learn.index12306.biz.payservice.remote;

import org.learn.index12306.biz.payservice.remote.dto.TicketOrderDetailRespDTO;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Milk
 * @version 2023/11/13 21:58
 */
@FeignClient(value = "index12306-order${unique-name:}-service")
public interface TicketOrderRemoteService {

    /**
     * 根据订单号查询车票订单
     */
    @GetMapping("/api/order-service/order/ticket/query")
    Result<TicketOrderDetailRespDTO> queryTicketOrderByOrderSn(@RequestParam(value = "orderSn") String orderSn);

}
