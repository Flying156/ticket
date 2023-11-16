package org.learn.index12306.biz.payservice.controller;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.payservice.convert.PayRequestConvert;
import org.learn.index12306.biz.payservice.dto.PayCommand;
import org.learn.index12306.biz.payservice.dto.PayInfoRespDTO;
import org.learn.index12306.biz.payservice.dto.PayRespDTO;
import org.learn.index12306.biz.payservice.dto.base.PayRequest;
import org.learn.index12306.biz.payservice.service.PayService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.*;

/**
 * 支付服务
 *
 * @author Milk
 * @version 2023/11/6 21:54
 */
@RestController
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /**
     * 公共支付接口
     */
    @PostMapping("/api/pay-service/pay/create")
    public Result<PayRespDTO> pay(@RequestBody PayCommand requestParam){
        PayRequest payRequest = PayRequestConvert.command2PayRequest(requestParam);
        return Results.success(payService.commonPay(payRequest));
    }

    /**
     * 根据订单号查询支付单详情
     */
    @GetMapping("/api/pay-service/pay/query/order-sn")
    public Result<PayInfoRespDTO> getPayInfoByOrderSn(@RequestParam(value = "orderSn") String orderSn){
        return Results.success(payService.getPayInfoByOrderSn(orderSn));
    }

    /**
     * 根据订单流水号查询支付单详情
     */
    @GetMapping("/api/pay-service/pay/query/pay-sn")
    public Result<PayInfoRespDTO> getPayInfoByPaySn(@RequestParam(value = "paySn") String paySn){
        return Results.success(payService.getPayInfoByPaySn(paySn));
    }
}
