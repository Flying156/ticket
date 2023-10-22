package org.learn.index12306.biz.ticketservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.index12306.biz.ticketservice.dao.entity.TicketDO;
import org.learn.index12306.biz.ticketservice.dto.req.PurchaseTicketReqDTO;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPageQueryRespDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPurchaseRespDTO;

/**
 * 车票接口层
 *
 * @author Milk
 * @version 2023/10/8 15:19
 */
public interface TicketService extends IService<TicketDO> {

    /**
     * 按照条件分页查询车票
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam);

    /**
     * 按照条件分页查询车票 V2
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    TicketPageQueryRespDTO pageListTicketQueryV2(TicketPageQueryReqDTO requestParam);

    /**
     * 购买车票
     *
     * @param requestParam 购买车票请求阐述
     * @return 购买车票返回结果
     */
    TicketPurchaseRespDTO purchaseTicketsV1(PurchaseTicketReqDTO requestParam);


    /**
     * 执行购买车票
     * 被对应购票版本号接口调用 {@link TicketService#purchaseTicketsV1(PurchaseTicketReqDTO)}
     *
     * @param requestParam 车票购买请求参数
     * @return 订单号和对应的购票结果
     */
    TicketPurchaseRespDTO executePurchaseTickets(PurchaseTicketReqDTO requestParam);
}
