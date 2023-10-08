package org.learn.index12306.biz.ticketservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dao.mapper.SeatMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.TicketMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainStationPriceMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainStationRelationMapper;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPageQueryRespDTO;
import org.learn.index12306.biz.ticketservice.service.TicketService;
import org.springframework.stereotype.Service;

/**
 * 车票接口层实现
 *
 * @author Milk
 * @version 2023/10/8 15:19
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {


    private final SeatMapper seatMapper;
    private final TicketMapper ticketMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;


    @Override
    public TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam) {
        return null;
    }
}
