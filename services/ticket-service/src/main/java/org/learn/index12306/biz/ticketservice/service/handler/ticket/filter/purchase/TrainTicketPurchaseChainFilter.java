package org.learn.index12306.biz.ticketservice.service.handler.ticket.filter.purchase;

import org.learn.index12306.biz.ticketservice.dto.req.PurchaseTicketReqDTO;
import org.learn.index12306.framework.starter.designpattern.chain.AbstractChainHandler;

import static org.learn.index12306.biz.ticketservice.common.enums.TicketChainMarkEnum.TRAIN_PURCHASE_TICKET_FILTER;

/**
 * 用户购买车票责任链过滤器
 *
 * @author Milk
 * @version 2023/10/12 19:34
 */
public interface TrainTicketPurchaseChainFilter<T extends PurchaseTicketReqDTO> extends AbstractChainHandler<PurchaseTicketReqDTO> {

    @Override
    default String mark(){
        return TRAIN_PURCHASE_TICKET_FILTER.name();
    }
}

