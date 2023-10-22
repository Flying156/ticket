package org.learn.index12306.biz.ticketservice.service.handler.ticket.filter.query;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Objects;

/**
 * 查询列车车票流程过滤器之基础数据验证
 *
 * @author Milk
 * @version 2023/10/9 17:16
 */
@Component
@RequiredArgsConstructor
public class TrainTicketQueryParamBaseVerifyChainFilter implements TrainTicketQueryChainFilter<TicketPageQueryReqDTO>{

    @Override
    public void handler(TicketPageQueryReqDTO requestParam) {
        if(requestParam.getDepartureDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isBefore(LocalDate.now())){
            throw new ClientException("出发时间不应该小于当前时间");
        }
        if(Objects.equals(requestParam.getFromStation(), requestParam.getToStation())){
            throw new ClientException("出发地和目的地不能相同");
        }
    }

    @Override
    public int getOrder() {
        return 10;
    }
}
