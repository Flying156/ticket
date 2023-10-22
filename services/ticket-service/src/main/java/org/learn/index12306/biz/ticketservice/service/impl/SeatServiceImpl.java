package org.learn.index12306.biz.ticketservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Arrays;
import org.learn.index12306.biz.ticketservice.common.enums.SeatStatusEnum;
import org.learn.index12306.biz.ticketservice.dao.entity.SeatDO;
import org.learn.index12306.biz.ticketservice.dao.mapper.SeatMapper;
import org.learn.index12306.biz.ticketservice.dto.domain.RouteDTO;
import org.learn.index12306.biz.ticketservice.service.SeatService;
import org.learn.index12306.biz.ticketservice.service.TrainStationService;
import org.learn.index12306.biz.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.learn.index12306.framework.statrer.cache.toolkit.CacheUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.TRAIN_STATION_CARRIAGE_REMAINING_TICKET;

/**
 * 座位接口实现
 *
 * @author Milk
 * @version 2023/10/13 20:48
 */
@Service
@RequiredArgsConstructor
public class SeatServiceImpl extends ServiceImpl<SeatMapper, SeatDO> implements SeatService {

    private final DistributedCache distributedCache;
    private final TrainStationService trainStationService;

    @Override
    public List<String> listUsableCarriageNumber(String trainId, Integer seatType, String departure, String arrival) {
        LambdaQueryWrapper<SeatDO>queryWrapper = Wrappers.lambdaQuery(SeatDO.class)
                .eq(SeatDO::getTrainId, trainId)
                .eq(SeatDO::getStartStation, departure)
                .eq(SeatDO::getEndStation, arrival)
                .eq(SeatDO::getSeatType, seatType)
                .eq(SeatDO::getSeatStatus, SeatStatusEnum.AVAILABLE.getCode())
                .groupBy(SeatDO::getCarriageNumber)
                .select(SeatDO::getCarriageNumber);
        List<SeatDO> seatDOList = baseMapper.selectList(queryWrapper);
        return seatDOList.stream().map(SeatDO::getCarriageNumber).toList();

    }


    @Override
    public List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList) {
        String keySuffix = CacheUtil.buildKey("_", trainId, departure, arrival);
        if(distributedCache.hasKey(TRAIN_STATION_CARRIAGE_REMAINING_TICKET + keySuffix)){
            StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
            List<Object> trainStationCarriageRemainingTicket =
                    stringRedisTemplate.opsForHash().multiGet(TRAIN_STATION_CARRIAGE_REMAINING_TICKET + keySuffix, Arrays.asList(trainCarriageList.toArray()));
            if(CollUtil.isNotEmpty(trainStationCarriageRemainingTicket)){
                return trainStationCarriageRemainingTicket.stream().map(each -> Integer.parseInt(each.toString())).toList();
            }
        }
        SeatDO seatDO = SeatDO.builder()
                .trainId(Long.parseLong(trainId))
                .startStation(departure)
                .endStation(arrival)
                .build();
        return baseMapper.listSeatRemainingTicket(seatDO, trainCarriageList);
    }

    @Override
    public List<String> listAvailableSeat(String trainId, String carriageNumber, Integer seatType, String departure, String arrival) {
        LambdaQueryWrapper<SeatDO> queryWrapper = Wrappers.lambdaQuery(SeatDO.class)
                .eq(SeatDO::getTrainId, trainId)
                .eq(SeatDO::getCarriageNumber, carriageNumber)
                .eq(SeatDO::getSeatType, seatType)
                .eq(SeatDO::getStartStation, departure)
                .eq(SeatDO::getEndStation, arrival)
                .eq(SeatDO::getSeatStatus, SeatStatusEnum.AVAILABLE.getCode())
                .select(SeatDO::getSeatNumber);
        List<SeatDO> seatDOList = baseMapper.selectList(queryWrapper);
        return seatDOList.stream().map(SeatDO::getSeatNumber).collect(Collectors.toList());
    }

    @Override
    public void lockSeat(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketRespList) {
        List<RouteDTO> routeList = trainStationService.listTakeoutTrainStationRoute(trainId, departure, arrival);
        trainPurchaseTicketRespList.forEach(each -> routeList.forEach(item ->{
            LambdaUpdateWrapper<SeatDO> updateWrapper = Wrappers.lambdaUpdate(SeatDO.class)
                    .eq(SeatDO::getTrainId, trainId)
                    .eq(SeatDO::getCarriageNumber, each.getCarriageNumber())
                    .eq(SeatDO::getStartStation, item.getStartStation())
                    .eq(SeatDO::getEndStation, item.getEndStation())
                    .eq(SeatDO::getSeatNumber, each.getSeatNumber());
            SeatDO updateSeatDO = SeatDO.builder()
                    .seatStatus(SeatStatusEnum.LOCKED.getCode())
                    .build();
            baseMapper.update(updateSeatDO, updateWrapper);
        }));
    }


    @Override
    public void unlock(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketResults) {
        List<RouteDTO> routeList = trainStationService.listTakeoutTrainStationRoute(trainId, departure, arrival);
        trainPurchaseTicketResults.forEach(each -> routeList.forEach(item ->{
            LambdaUpdateWrapper<SeatDO> updateWrapper = Wrappers.lambdaUpdate(SeatDO.class)
                    .eq(SeatDO::getTrainId, trainId)
                    .eq(SeatDO::getCarriageNumber, each.getCarriageNumber())
                    .eq(SeatDO::getStartStation, item.getStartStation())
                    .eq(SeatDO::getEndStation, item.getEndStation())
                    .eq(SeatDO::getSeatNumber, each.getSeatNumber());
            SeatDO updateSeatDO = SeatDO.builder()
                    .seatStatus(SeatStatusEnum.AVAILABLE.getCode())
                    .build();
            baseMapper.update(updateSeatDO, updateWrapper);
        }));
    }
}
