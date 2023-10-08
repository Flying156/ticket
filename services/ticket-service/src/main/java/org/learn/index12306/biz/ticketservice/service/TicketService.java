package org.learn.index12306.biz.ticketservice.service;

import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPageQueryRespDTO;

/**
 * 车票接口层
 *
 * @author Milk
 * @version 2023/10/8 15:19
 */
public interface TicketService {

    /**
     * 按照条件分页查询车票
     *
     * @param requestParam 分页查询车票请求参数
     * @return 查询车票返回结果
     */
    TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam);
}
