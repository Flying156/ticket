package org.learn.index12306.biz.ticketservice.service.cache;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.common.enums.SeatStatusEnum;
import org.learn.index12306.biz.ticketservice.common.enums.VehicleTypeEnum;
import org.learn.index12306.biz.ticketservice.dao.entity.SeatDO;
import org.learn.index12306.biz.ticketservice.dao.entity.TrainDO;
import org.learn.index12306.biz.ticketservice.dao.entity.TrainStationPriceDO;
import org.learn.index12306.biz.ticketservice.dao.mapper.SeatMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainStationMapper;
import org.learn.index12306.biz.ticketservice.dto.domain.RouteDTO;
import org.learn.index12306.biz.ticketservice.service.TrainStationService;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.learn.index12306.framework.statrer.cache.toolkit.CacheUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.learn.index12306.biz.ticketservice.common.constant.Index12306Constant.ADVANCE_TICKET_DAY;
import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.*;

/**
 * 座位余量缓存加载
 *
 * @author Milk
 * @version 2023/10/10 16:25
 */
@Component
@RequiredArgsConstructor
public class SeatMarginCacheLoader {

    private final SeatMapper seatMapper;
    private final TrainMapper trainMapper;
    private final TrainStationService trainStationService;
    private final RedissonClient redissonClient;
    private final DistributedCache distributedCache;

    /**
     * 加载区间内的所有班次的各种座位数量
     *
     * @param trainId   列车 ID
     * @param seatType  座位类型
     * @param departure 出发站
     * @param arrival   终点站
     * @return  该班次的各种座位类型的剩余数量
     */
    public Map<String, String> load(String trainId, String seatType, String departure, String arrival){

        Map<String, Map<String, String>> trainStationRemainingTicketMaps = new LinkedHashMap<>();
        String keySuffix = CacheUtil.buildKey(trainId, departure, arrival);

        RLock lock = redissonClient.getLock(String.format(LOCK_SAFE_LOAD_SEAT_MARGIN_GET, trainId));
        lock.lock();
        try{
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            Object quantityObj = stringRedisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + keySuffix, seatType);
            if(CacheUtil.isNullOrBlank(quantityObj)){
                // 获取列车详细信息
                TrainDO trainDO = distributedCache.safeGet(
                        TRAIN_INFO + trainId,
                        TrainDO.class,
                        () -> trainMapper.selectById(trainId),
                        ADVANCE_TICKET_DAY,
                        TimeUnit.DAYS
                );
                // 获取所有出发站点到目的站点的所有组合
                List<RouteDTO> routeList = trainStationService.listTrainStationRoute(trainId, trainDO.getStartStation(), trainDO.getEndStation());
                if(CollUtil.isNotEmpty(routeList)){
                    // 根据列车类型查找座位信息以及剩余的座位数量
                    switch(trainDO.getTrainType()){

                        case 0 -> {
                            for (RouteDTO each : routeList) {
                                Map<String, String> trainStationRemainingTicket = new LinkedHashMap<>();
                                trainStationRemainingTicket.put("0", selectSeatMargin(trainId, 0, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("1", selectSeatMargin(trainId, 1, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("2", selectSeatMargin(trainId, 2, each.getStartStation(), each.getEndStation()));
                                String actualKeySuffix = CacheUtil.buildKey(trainId, each.getStartStation(), each.getEndStation());
                                trainStationRemainingTicketMaps.put(TRAIN_STATION_REMAINING_TICKET + actualKeySuffix, trainStationRemainingTicket);
                            }
                        }

                        case 1->{
                            for(RouteDTO each : routeList){
                                Map<String, String> trainStationRemainingTicket = new LinkedHashMap<>();
                                trainStationRemainingTicket.put("3", selectSeatMargin(trainId, 3, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("4", selectSeatMargin(trainId, 4, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("5", selectSeatMargin(trainId, 5, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("13", selectSeatMargin(trainId, 13, each.getStartStation(), each.getEndStation()));
                                String actualKeySuffix = CacheUtil.buildKey(trainId, each.getStartStation(), each.getEndStation());
                                trainStationRemainingTicketMaps.put(TRAIN_STATION_REMAINING_TICKET + actualKeySuffix, trainStationRemainingTicket);
                            }
                        }

                        case 2 ->{
                            for(RouteDTO each : routeList){
                                Map<String, String> trainStationRemainingTicket = new LinkedHashMap<>();
                                trainStationRemainingTicket.put("6", selectSeatMargin(trainId, 6, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("7", selectSeatMargin(trainId, 7, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("8", selectSeatMargin(trainId, 8, each.getStartStation(), each.getEndStation()));
                                trainStationRemainingTicket.put("13", selectSeatMargin(trainId, 13, each.getStartStation(), each.getEndStation()));
                                String actualKeySuffix = CacheUtil.buildKey(trainId, each.getStartStation(), each.getEndStation());
                                trainStationRemainingTicketMaps.put(TRAIN_STATION_REMAINING_TICKET + actualKeySuffix, trainStationRemainingTicket);
                            }
                        }

                    }
                }else{
                    Map<String, String> trainStationRemainingTicket = new LinkedHashMap<>();
                    // 将所有座位数量设置为 0
                    VehicleTypeEnum.findSeatTypesByCode(trainDO.getTrainType()).forEach(
                            each -> trainStationRemainingTicket.put(String.valueOf(each), "0"));
                    trainStationRemainingTicketMaps.put(TRAIN_STATION_REMAINING_TICKET + keySuffix, trainStationRemainingTicket);
                }

                // TODO LUA脚本操作
                // 将区间内的余票放入缓存中
                trainStationRemainingTicketMaps.forEach((cacheKey, cacheMap) -> stringRedisTemplate.opsForHash().putAll(cacheKey, cacheMap));
            }
        }finally {
            lock.unlock();
        }
        return Optional.ofNullable(trainStationRemainingTicketMaps.get(TRAIN_STATION_REMAINING_TICKET + keySuffix))
                .orElse(new LinkedHashMap<>());
    }

    private String selectSeatMargin(String trainId, Integer type, String departure, String arrival){
        LambdaQueryWrapper<SeatDO>queryWrapper = Wrappers.lambdaQuery(SeatDO.class)
                .eq(SeatDO::getTrainId, trainId)
                .eq(SeatDO::getSeatType, type)
                .eq(SeatDO::getSeatStatus, SeatStatusEnum.AVAILABLE.getCode())
                .eq(SeatDO::getStartStation, departure)
                .eq(SeatDO::getEndStation, arrival);
        return Optional.ofNullable(seatMapper.selectCount(queryWrapper))
                .map(String::valueOf)
                .orElse("0");
    }

}
