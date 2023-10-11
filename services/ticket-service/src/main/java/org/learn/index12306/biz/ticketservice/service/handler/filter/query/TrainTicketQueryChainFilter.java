package org.learn.index12306.biz.ticketservice.service.handler.filter.query;

import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.framework.starter.designpattern.chain.AbstractChainHandler;

import static org.learn.index12306.biz.ticketservice.common.enums.TicketChainMarkEnum.TRAIN_QUERY_TICKET_FILTER;

/**
 * 用户查询车票责任链过滤器
 *
 * @author Milk
 * @version 2023/10/8 16:55
 */
public interface TrainTicketQueryChainFilter<T extends TicketPageQueryReqDTO> extends AbstractChainHandler<TicketPageQueryReqDTO> {

    /**
     * 查询车票责任链过滤器标识
     */
    @Override
    default String mark(){
        return TRAIN_QUERY_TICKET_FILTER.name();
    }
}
