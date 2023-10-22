package org.learn.index12306.biz.ticketservice.service.handler.ticket.filter.purchase;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.domain.PurchaseTicketPassengerDetailDTO;
import org.learn.index12306.biz.ticketservice.dto.req.PurchaseTicketReqDTO;
import org.learn.index12306.biz.ticketservice.service.cache.SeatMarginCacheLoader;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET;

/**
 * 购票流程过滤器之验证余票是否充足
 *
 * @author Milk
 * @version 2023/10/12 20:20
 */
@Component
@RequiredArgsConstructor
public class TrainPurchaseTicketParamStockChainHandler implements TrainTicketPurchaseChainFilter<PurchaseTicketReqDTO>{

    private final DistributedCache distributedCache;
    private final SeatMarginCacheLoader seatMarginCacheLoader;


    @Override
    public void handler(PurchaseTicketReqDTO requestParam) {
        String keySuffix = StrUtil.join("_", requestParam.getTrainId(), requestParam.getDeparture(), requestParam.getArrival());
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        List<PurchaseTicketPassengerDetailDTO> passengerDetailList = requestParam.getPassengers();
        Map<Integer, List<PurchaseTicketPassengerDetailDTO>> seatTypeMap = passengerDetailList.stream().collect(Collectors.groupingBy(PurchaseTicketPassengerDetailDTO::getSeatType));
        seatTypeMap.forEach((seatType, passengerList) ->{
            Object stockObj = stringRedisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + keySuffix, String.valueOf(seatType));
            int stock = Optional.ofNullable(stockObj).map((each -> Integer.parseInt(each.toString()))).orElseGet(() ->{
                Map<String, String> seatMarginMap = seatMarginCacheLoader.load(String.valueOf(requestParam.getTrainId()), String.valueOf(seatType), requestParam.getDeparture(), requestParam.getArrival());
                return Optional.ofNullable(seatMarginMap.get(String.valueOf(seatType))).map(Integer::parseInt).orElse(0);
            });
            if(stock < passengerList.size()){
                throw new ClientException("列车站点已无余票");
            }
        });
    }

    @Override
    public int getOrder() {
        return 20;
    }
}
