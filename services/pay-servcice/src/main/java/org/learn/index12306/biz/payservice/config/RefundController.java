package org.learn.index12306.biz.payservice.config;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.payservice.dto.RefundReqDTO;
import org.learn.index12306.biz.payservice.dto.RefundRespDTO;
import org.learn.index12306.biz.payservice.service.RefundService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 退款控制器
 *
 * @author Milk
 * @version 2023/11/13 21:24
 */
@RestController
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping("/api/pay-service/common/refund")
    public Result<RefundRespDTO> commonRefund(@RequestBody RefundReqDTO requestParam){
        return Results.success(refundService.refund(requestParam));
    }

}
