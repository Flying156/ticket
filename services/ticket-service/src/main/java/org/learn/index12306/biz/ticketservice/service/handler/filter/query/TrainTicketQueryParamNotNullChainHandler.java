package org.learn.index12306.biz.ticketservice.service.handler.filter.query;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.springframework.stereotype.Component;

/**
 * 查询列车车票流程过滤器之验证数据是否为空或空的字符串
 *
 * @author Milk
 * @version 2023/10/8 17:05
 */
@Component
@RequiredArgsConstructor
public class TrainTicketQueryParamNotNullChainHandler implements TrainTicketQueryChainFilter<TicketPageQueryReqDTO>{

    @Override
    public void handler(TicketPageQueryReqDTO requestParam) {
        if(requestParam != null){
            if(StrUtil.isBlank(requestParam.getFromStation())){
                throw new ClientException("出发地不能为空");
            }
            if(StrUtil.isBlank(requestParam.getArrival())){
                throw new ClientException("目的地不能为空");
            }
            if (requestParam.getDepartureDate() == null) {
                throw new ClientException("出发日期不能为空");
            }
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
