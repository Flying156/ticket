package org.learn.index12306.biz.ticketservice.service.handler.ticket.base;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import org.learn.index12306.biz.ticketservice.dto.domain.RouteDTO;
import org.learn.index12306.biz.ticketservice.dto.domain.TrainSeatBaseDTO;
import org.learn.index12306.biz.ticketservice.service.TrainStationService;
import org.learn.index12306.biz.ticketservice.service.handler.ticket.dto.SelectSeatDTO;
import org.learn.index12306.biz.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;
import org.learn.index12306.framework.starter.bases.ApplicationContextHolder;
import org.learn.index12306.framework.starter.designpattern.strategy.AbstractExecuteStrategy;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.learn.index12306.framework.statrer.cache.toolkit.CacheUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_REMAINING_TICKET;

/**
 * 抽象高铁购票模版基础服务
 *
 * @author Milk
 * @version 2023/10/13 20:58
 */
public abstract class AbstractTrainPurchaseTicketTemplate implements IPurchaseTicket, CommandLineRunner, AbstractExecuteStrategy<SelectSeatDTO, List<TrainPurchaseTicketRespDTO>> {

    private DistributedCache distributedCache;
    private String ticketAvailabilityCacheUpdateType;
    private TrainStationService trainStationService;



    protected TrainSeatBaseDTO buildTrainSeatBaseDTO(SelectSeatDTO requestParam){
        return TrainSeatBaseDTO.builder()
                .trainId(requestParam.getRequestParam().getTrainId())
                .departure(requestParam.getRequestParam().getDeparture())
                .arrival(requestParam.getRequestParam().getArrival())
                .passengerSeatDetails(requestParam.getPassengerSeatDetails())
                .chooseSeatList(requestParam.getRequestParam().getChooseSeats())
                .build();
    }

    /**
     * 选择座位
     *
     * @param requestParam 购票请求入参
     * @return 乘车人座位
     */
    protected abstract List<TrainPurchaseTicketRespDTO> selectSeats(SelectSeatDTO requestParam);

    @Override
    public List<TrainPurchaseTicketRespDTO> executeResp(SelectSeatDTO requestParam) {
        List<TrainPurchaseTicketRespDTO> actualResult = selectSeats(requestParam);
        // 扣减车厢缓存，扣减站点余票缓存
        if(CollUtil.isNotEmpty(actualResult) && !StrUtil.equals(ticketAvailabilityCacheUpdateType, "binlog")){
            String trainId = requestParam.getRequestParam().getTrainId();
            String departure = requestParam.getRequestParam().getDeparture();
            String arrival = requestParam.getRequestParam().getArrival();
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            List<RouteDTO> routeDTOList = trainStationService.listTrainStationRoute(trainId, departure, arrival);
            routeDTOList.forEach(each ->{
                String keySuffix = CacheUtil.buildKey("_", trainId, each.getStartStation(), each.getEndStation());
                stringRedisTemplate.opsForHash().increment(TRAIN_STATION_REMAINING_TICKET + keySuffix, String.valueOf(requestParam.getSeatType()), -actualResult.size());
            });

        }
        return actualResult;
    }

    @Override
    public void run(String... args) throws Exception {
        distributedCache = ApplicationContextHolder.getBean(DistributedCache.class);
        trainStationService = ApplicationContextHolder.getBean(TrainStationService.class);
        ConfigurableEnvironment configurableEnvironment = ApplicationContextHolder.getBean(ConfigurableEnvironment.class);
        ticketAvailabilityCacheUpdateType = configurableEnvironment.getProperty("ticket.availability.cache-update.type", "");
    }
}
