package org.learn.index12306.biz.ticketservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.common.enums.VehicleSeatTypeEnum;
import org.learn.index12306.biz.ticketservice.common.enums.VehicleTypeEnum;
import org.learn.index12306.biz.ticketservice.dao.entity.*;
import org.learn.index12306.biz.ticketservice.dao.mapper.*;
import org.learn.index12306.biz.ticketservice.dto.domain.SeatClassDTO;
import org.learn.index12306.biz.ticketservice.dto.domain.TicketListDTO;
import org.learn.index12306.biz.ticketservice.dto.req.TicketPageQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TicketPageQueryRespDTO;
import org.learn.index12306.biz.ticketservice.service.TicketService;
import org.learn.index12306.biz.ticketservice.service.cache.SeatMarginCacheLoader;
import org.learn.index12306.biz.ticketservice.toolkit.TimeStringComparator;
import org.learn.index12306.framework.starter.designpattern.chain.AbstractChainContext;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.learn.index12306.framework.statrer.cache.toolkit.CacheUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.learn.index12306.biz.ticketservice.common.constant.Index12306Constant.ADVANCE_TICKET_DAY;
import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.*;
import static org.learn.index12306.biz.ticketservice.common.enums.TicketChainMarkEnum.TRAIN_QUERY_TICKET_FILTER;
import static org.learn.index12306.biz.ticketservice.toolkit.DateUtil.calculateHourDifference;
import static org.learn.index12306.biz.ticketservice.toolkit.DateUtil.convertDateToLocalTime;

/**
 * 车票接口层实现
 *
 * @author Milk
 * @version 2023/10/8 15:19
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketMapper ticketMapper;
    private final SeatMapper seatMapper;
    private final CarriageMapper carriageMapper;
    private final TrainMapper trainMapper;
    private final StationMapper stationMapper;
    private final RegionMapper regionMapper;
    private final TrainStationPriceMapper trainStationPriceMapper;
    private final TrainStationRelationMapper trainStationRelationMapper;
    private final SeatMarginCacheLoader seatMarginCacheLoader;
    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;
    private final AbstractChainContext<TicketPageQueryReqDTO> chainContext;


    @Value("${ticket.availability.cache-update.type:}")
    private String ticketAvailabilityCacheUpdateType;
    @Value("${framework.cache.redis.prefix:}")
    private String cacheRedisPrefix;


    /**
     * 查询车票 v1,需要多次连接 redis 并使用分布式锁，性能拉跨
     *
     * @param requestParam 分页查询车票请求参数
     */
    @Override
    public TicketPageQueryRespDTO pageListTicketQueryV1(TicketPageQueryReqDTO requestParam) {
        // 责任链模式验证城市名称是否存在，出发日期不能小于当前日期
        chainContext.handler(TRAIN_QUERY_TICKET_FILTER.name(), requestParam);
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        // 通过映射获取出发站和到达站的名称
        List<Object> stationDetails = stringRedisTemplate.opsForHash()
                .multiGet(REGION_TRAIN_STATION_MAPPING,  Lists.newArrayList(requestParam.getFromStation(), requestParam.getToStation()));
        long count = stationDetails.stream().filter(Objects::isNull).count();
        // 缓存中没有code、名称映射，由于通过验证，需要重新加载缓存
        if(count > 0){
            RLock lock = redissonClient.getLock(LOCK_REGION_TRAIN_STATION_MAPPING);
            lock.lock();
            try{
                stationDetails = stringRedisTemplate.opsForHash()
                        .multiGet(REGION_TRAIN_STATION_MAPPING, Lists.newArrayList(requestParam.getFromStation(), requestParam.getToStation()));
                count = stationDetails.stream().filter(Objects::isNull).count();
                if(count > 0){
                    List<StationDO> stationDOList = stationMapper.selectList(Wrappers.emptyWrapper());
                    Map<String, String> regionTrainStationMap = new HashMap<>();
                    stationDOList.forEach(each -> regionTrainStationMap.put(each.getCode(), each.getName()));
                    stringRedisTemplate.opsForHash().putAll(REGION_TRAIN_STATION_MAPPING, regionTrainStationMap);
                    stationDetails = new ArrayList<>();
                    stationDetails.add(regionTrainStationMap.get(requestParam.getFromStation()));
                    stationDetails.add(regionTrainStationMap.get(requestParam.getFromStation()));
                }
            }finally {
                lock.unlock();
            }
        }
        // 查询起始站点到目标站的所有列车信息（当前不包括座位）
        List<TicketListDTO> seatResults = new ArrayList<>();
        String buildRegionTrainStationHashKey = String.format(REGION_TRAIN_STATION, stationDetails.get(0), stationDetails.get(1));
        Map<Object, Object> regionTrainStationAllMap = stringRedisTemplate.opsForHash().entries(REGION_TRAIN_STATION);
        if(MapUtil.isEmpty(regionTrainStationAllMap)){
            RLock lock = redissonClient.getLock(LOCK_REGION_TRAIN_STATION);
            lock.lock();
            try{
                regionTrainStationAllMap = stringRedisTemplate.opsForHash().entries(buildRegionTrainStationHashKey);
                if(MapUtil.isEmpty(regionTrainStationAllMap)){
                    LambdaQueryWrapper<TrainStationRelationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationRelationDO.class)
                            .eq(TrainStationRelationDO::getStartRegion, stationDetails.get(0))
                            .eq(TrainStationRelationDO::getEndRegion, stationDetails.get(1));
                    List<TrainStationRelationDO> trainStationRelationDOList = trainStationRelationMapper.selectList(queryWrapper);
                    for(TrainStationRelationDO each : trainStationRelationDOList){
                        TrainDO trainDO = distributedCache.safeGet(
                                TRAIN_INFO + each.getTrainId(),
                                TrainDO.class,
                                () -> trainMapper.selectById(each.getTrainId()),
                                ADVANCE_TICKET_DAY,
                                TimeUnit.DAYS
                        );
                        // 封装数据
                        TicketListDTO ticketListDTO = new TicketListDTO();
                        ticketListDTO.setTrainId(trainDO.getId());
                        ticketListDTO.setTrainNumber(trainDO.getTrainNumber());
                        ticketListDTO.setDepartureTime(convertDateToLocalTime(trainDO.getDepartureTime(), "HH:mm"));
                        ticketListDTO.setArrivalTime(convertDateToLocalTime(trainDO.getArrivalTime(), "HH:mm"));
                        ticketListDTO.setDuration(calculateHourDifference(trainDO.getDepartureTime(), trainDO.getArrivalTime()));
                        ticketListDTO.setDeparture(each.getDeparture());
                        ticketListDTO.setArrival(each.getArrival());
                        ticketListDTO.setDepartureFlag(each.getDepartureFlag());
                        ticketListDTO.setArrivalFlag(each.getArrivalFlag());
                        ticketListDTO.setTrainType(trainDO.getTrainType());
                        ticketListDTO.setTrainBrand(trainDO.getTrainBrand());
                        if(StrUtil.isNotBlank(trainDO.getTrainTag())){
                            ticketListDTO.setTrainTags(StrUtil.split(trainDO.getTrainTag(), ","));
                        }
                        long betweenDay = DateUtil.betweenDay(each.getDepartureTime(), each.getArrivalTime(), false);
                        ticketListDTO.setDaysArrived((int) betweenDay);
                        ticketListDTO.setSaleStatus(new Date().after(trainDO.getSaleTime()) ? 0 : 1);
                        seatResults.add(ticketListDTO);
                        // 将结果放入哈希表中
                        regionTrainStationAllMap.put(CacheUtil.buildKey(String.valueOf(each.getTrainId()), each.getDeparture(), each.getArrival()), JSON.toJSONString(ticketListDTO));
                    }
                    // 将查询结果放入缓存中
                    stringRedisTemplate.opsForHash().putAll(buildRegionTrainStationHashKey, regionTrainStationAllMap);
                }
            }finally{
                lock.unlock();
            }
        }
        // 获取座位集合
        seatResults = CollUtil.isEmpty(seatResults)
                ? regionTrainStationAllMap.values().stream().map(each -> JSON.parseObject(each.toString(), TicketListDTO.class)).collect(Collectors.toList())
                : seatResults;
        seatResults =  seatResults.stream().sorted(new TimeStringComparator()).toList();

        // 查询相对应的座位以及价格
        for(TicketListDTO each: seatResults){
            // 将每班列车的每种座位价格加入缓存
            String trainStationPriceStr =  distributedCache.safeGet(
                    String.format(TRAIN_STATION_PRICE, each.getTrainId(), each.getDeparture(), each.getArrival()),
                    String.class,
                    () ->{
                        LambdaQueryWrapper<TrainStationPriceDO>queryWrapper = Wrappers.lambdaQuery(TrainStationPriceDO.class)
                                .eq(TrainStationPriceDO::getTrainId, each.getTrainId())
                                .eq(TrainStationPriceDO::getDeparture, each.getDeparture())
                                .eq(TrainStationPriceDO::getArrival, each.getArrival());
                        return JSON.toJSONString(trainStationPriceMapper.selectList(queryWrapper));
                    },
                    ADVANCE_TICKET_DAY,
                    TimeUnit.DAYS
            );
            List<TrainStationPriceDO> trainStationPriceDOList = JSON.parseArray(trainStationPriceStr, TrainStationPriceDO.class);
            // 查找座位各种类型数量
            List<SeatClassDTO> seatClassList = new ArrayList<>();
            trainStationPriceDOList.forEach(item ->{
                String seatType = String.valueOf(item.getSeatType());
                String keySuffix = StrUtil.join("_", each.getTrainId(), item.getDeparture(), item.getArrival());
                // 获取特定的座位价格
                Object quantityObj = stringRedisTemplate.opsForHash().get(TRAIN_STATION_REMAINING_TICKET + keySuffix, seatType);
                int quantity = Optional.ofNullable(quantityObj)
                        .map(Object::toString)
                        .map(Integer::parseInt)
                        .orElseGet(() ->{
                            // 如果缓存中没有就从数据库加载
                            Map<String, String> seatMarginMap = seatMarginCacheLoader.load(String.valueOf(each.getTrainId()),seatType, item.getDeparture(), item.getArrival());
                            return Optional.ofNullable(seatMarginMap.get(String.valueOf(item.getSeatType()))).map(Integer::parseInt).orElse(0);

                        });
                seatClassList.add(new SeatClassDTO(item.getSeatType(), quantity, new BigDecimal(item.getPrice()).divide(new BigDecimal(100), 1, RoundingMode.HALF_UP) , false));
            });
            each.setSeatClassList(seatClassList);
        }


        return TicketPageQueryRespDTO.builder()
                .trainList(seatResults)
                .departureStationList(buildDepartureStationList(seatResults))
                .arrivalStationList(buildArrivalStationList(seatResults))
                .trainBrandList(buildTrainBrandList(seatResults))
                .seatClassTypeList(buildSeatClassList(seatResults))
                .build();
    }


    /**
     * 相比V1版本的区别是通过其他手段在空闲的时候查询数据库并
     * 更新缓存，避免查询时频繁连接redis导致的性能瓶颈
     */
    @Override
    public TicketPageQueryRespDTO pageListTicketQueryV2(TicketPageQueryReqDTO requestParam) {
        // 责任链模式过滤请求参数
        chainContext.handler(TRAIN_QUERY_TICKET_FILTER.name(), requestParam);
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) distributedCache.getInstance();
        List<Object> stationDetails = stringRedisTemplate.opsForHash()
                .multiGet(REGION_TRAIN_STATION_MAPPING, Lists.newArrayList(requestParam.getFromStation(), requestParam.getToStation()));
        // 构建 key,从缓存中查询 起始站--> 出发站 的所有列车信息
        String buildRegionTrainStationHashKey = String.format(REGION_TRAIN_STATION, stationDetails.get(0), stationDetails.get(1));
        Map<Object, Object> regionTrainStationAllMap = stringRedisTemplate.opsForHash().entries(buildRegionTrainStationHashKey);
        // 获取线路的各种信息,不包含座位的各种信息
        List<TicketListDTO> seatResults = regionTrainStationAllMap.values().stream()
                .map(each -> JSON.parseObject(each.toString(), TicketListDTO.class))
                .sorted(new TimeStringComparator())
                .toList();

        List<String> trainStationPriceKeys = seatResults.stream()
                .map(each -> String.format( TRAIN_STATION_PRICE, each.getTrainId(), each.getDeparture(), each.getArrival()))
                .toList();

        List<Object> trainStationPriceObjs = stringRedisTemplate.executePipelined((RedisCallback<String>) connection -> {
            trainStationPriceKeys.forEach(each -> connection.stringCommands().get(each.getBytes()));
            return null;
        });


        List<TrainStationPriceDO> trainStationPriceDOList = new ArrayList<>();
        List<String> trainStationRemainingKeyList = new ArrayList<>();
        for(Object each : trainStationPriceObjs){
            List<TrainStationPriceDO> trainStationPriceList = JSON.parseArray(each.toString(), TrainStationPriceDO.class);
            trainStationPriceDOList.addAll(trainStationPriceList);
            for(TrainStationPriceDO item : trainStationPriceList){
                String trainStationRemainingKey =  TRAIN_STATION_REMAINING_TICKET + StrUtil.join("_",item.getTrainId(), item.getDeparture(), item.getArrival());
                trainStationRemainingKeyList.add(trainStationRemainingKey);
            }
        }
        // 获取各种座位剩余数量
        List<Object> trainStationRemainingObjs = stringRedisTemplate.executePipelined((RedisCallback<String>)connection ->{
            for(int i = 0; i < trainStationRemainingKeyList.size(); i++){
                connection.hashCommands().hGet(trainStationRemainingKeyList.get(i).getBytes(), trainStationPriceDOList.get(i).getSeatType().toString().getBytes());
            }
            return null;
        });


        for(TicketListDTO each : seatResults){
            List<Integer> seatTypesByCode = VehicleTypeEnum.findSeatTypesByCode(each.getTrainType());
            List<Object> remainingTicket = new ArrayList<>(trainStationRemainingObjs.subList(0, seatTypesByCode.size()));
            List<TrainStationPriceDO> trainStationPriceDOSub = new ArrayList<>(trainStationPriceDOList.subList(0, seatTypesByCode.size()));
            trainStationRemainingObjs.subList(0, seatTypesByCode.size()).clear();
            trainStationPriceDOList.subList(0, seatTypesByCode.size()).clear();
            List<SeatClassDTO> seatClassDTOList = new ArrayList<>();

            for (int i = 0; i < trainStationPriceDOSub.size(); i++) {
                TrainStationPriceDO trainStationPriceDO = trainStationPriceDOSub.get(i);
                SeatClassDTO seatClassDTO = SeatClassDTO.builder()
                        .type(trainStationPriceDO.getSeatType())
                        .price(new BigDecimal(trainStationPriceDO.getPrice()).divide(new BigDecimal("100"), 1, RoundingMode.HALF_UP))
                        .quantity(Integer.parseInt(remainingTicket.get(i).toString()))
                        .candidate(false)
                        .build();
                seatClassDTOList.add(seatClassDTO);
            }
            each.setSeatClassList(seatClassDTOList);
        }


        return TicketPageQueryRespDTO.builder()
                .trainList(seatResults)
                .departureStationList(buildDepartureStationList(seatResults))
                .arrivalStationList(buildArrivalStationList(seatResults))
                .seatClassTypeList(buildSeatClassList(seatResults))

                .build();
    }

    /**
     * 构建出发站点集合
     */
    private List<String> buildDepartureStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getDeparture).distinct().toList();
    }

    /**
     * 构建到达站点集合
     */
    private List<String> buildArrivalStationList(List<TicketListDTO> seatResults) {
        return seatResults.stream().map(TicketListDTO::getArrival).distinct().collect(Collectors.toList());
    }

    /**
     * 构建座位类型集合
     */
    private List<Integer> buildSeatClassList(List<TicketListDTO> seatResults){
        Set<Integer> resultSeatClassList = new HashSet<>();
        for(TicketListDTO each: seatResults){
            for (SeatClassDTO item : each.getSeatClassList()) {
                resultSeatClassList.add(item.getType());
            }
        }
        return resultSeatClassList.stream().toList();
    }

    /**
     * 构建列车类型集合
     */
    private List<Integer> buildTrainBrandList(List<TicketListDTO> seatResults) {
        Set<Integer> trainBrandSet = new HashSet<>();
        for (TicketListDTO each : seatResults) {
            if (StrUtil.isNotBlank(each.getTrainBrand())) {
                trainBrandSet.addAll(StrUtil.split(each.getTrainBrand(), ",").stream().map(Integer::parseInt).toList());
            }
        }
        return trainBrandSet.stream().toList();
    }


}
