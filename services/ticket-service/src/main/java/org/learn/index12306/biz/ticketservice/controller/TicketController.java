package org.learn.index12306.biz.ticketservice.controller;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.req.PurchaseTicketReqDTO;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPageQueryRespDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPurchaseRespDTO;
import org.learn.index12306.biz.ticketservice.service.TicketService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.idempotent.annotation.Idempotent;
import org.learn.index12306.framework.starter.idempotent.enums.IdempotentSceneEnum;
import org.learn.index12306.framework.starter.idempotent.enums.IdempotentTypeEnum;
import org.learn.index12306.framework.starter.log.annotation.ILog;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.*;

/**
 * 车票控制层
 *
 * @author Milk
 * @version 2023/10/8 15:18
 */
@RestController
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    /**
     * 根据条件查询符合条件的列车
     */
    @GetMapping("/api/ticket-service/ticket/query")
    public Result<TicketPageQueryRespDTO> pageListTicketQuery(TicketPageQueryReqDTO requestParam){
        return Results.success(ticketService.pageListTicketQueryV1(requestParam));
    }


    /**
     * 购买车票
     */
    @ILog
    @Idempotent(
            uniqueKeyPrefix = "index12306-ticket:lock_purchase-tickets:",
            key = "T(org.learn.index12306.framework.starter.bases.ApplicationContextHolder).getBean('environment').getProperty('unique-name', '')"
                    + "+'_'+"
                    + "T(org.learn.index12306.framework.starter.user.core.UserContext).getUsername()",
            message = "正在执行下单流程，请稍后...",
            scene = IdempotentSceneEnum.RESTAPI,
            type = IdempotentTypeEnum.SPEL
    )
    public Result<TicketPurchaseRespDTO> purchaseTickets(@RequestBody PurchaseTicketReqDTO requestParam){
        return Results.success(ticketService.purchaseTicketsV1(requestParam));
    }


    @ILog
    @Idempotent(
            uniqueKeyPrefix = "index12306-ticket:lock_purchase-tickets:",
            key = "T(org.learn.index12306.framework.starter.bases.ApplicationContextHolder).getBean('environment').getProperty('unique-name', '')"
                    + "+'_'+"
                    + "T(org.learn.index12306.framework.starter.user.core.UserContext).getUsername()",
            message = "正在指向下单流程，请稍后...",
            scene = IdempotentSceneEnum.RESTAPI,
            type = IdempotentTypeEnum.SPEL
    )
    @PostMapping("/api/ticket-service/ticket/purchase/v2")
    public Result<TicketPurchaseRespDTO> purchaseTicketsV2(@RequestBody PurchaseTicketReqDTO requestParam){
        return Results.success(ticketService.purchaseTicketsV2(requestParam));
    }

}
