package org.learn.index12306.biz.ticketservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.common.enums.RegionStationQueryTypeEnum;
import org.learn.index12306.biz.ticketservice.dao.entity.RegionDO;
import org.learn.index12306.biz.ticketservice.dao.mapper.RegionMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.StationMapper;
import org.learn.index12306.biz.ticketservice.dto.req.RegionStationQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.RegionStationQueryRespDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.StationQueryRespDTO;
import org.learn.index12306.biz.ticketservice.service.RegionStationService;
import org.learn.index12306.framework.starter.common.enums.FlagEnum;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.learn.index12306.framework.statrer.cache.core.CacheLoader;
import org.learn.index12306.framework.statrer.cache.toolkit.CacheUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.learn.index12306.biz.ticketservice.common.constant.Index12306Constant.ADVANCE_TICKET_DAY;
import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.*;

/**
 * 地区以及车站接口层实现层
 *
 * @author Milk
 * @version 2023/10/7 16:36
 */
@Service
@RequiredArgsConstructor
public class RegionStationServiceImpl implements RegionStationService {

    private final RegionMapper regionMapper;
    private final StationMapper stationMapper;
    private final DistributedCache distributedCache;
    private final RedissonClient redissonClient;

    @Override
    public List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam) {
        String key;
        if(StrUtil.isNotBlank(requestParam.getName())){
            key = REGION_STATION + requestParam.getName();
            return safeGetRegionStation(
                key,
                () ->{
                    LambdaQueryWrapper<RegionDO> queryWrapper = Wrappers.lambdaQuery(RegionDO.class)
                            .likeRight(RegionDO::getFullName, requestParam.getName())
                            .or()
                            .likeRight(RegionDO::getSpell, requestParam.getName());
                    List<RegionDO>regionList = regionMapper.selectList(queryWrapper);
                    return JSON.toJSONString(BeanUtil.convert(regionList, RegionStationQueryRespDTO.class));
                },
                requestParam.getName());
        }
        key = REGION_STATION + requestParam.getQueryType();
        LambdaQueryWrapper<RegionDO> queryWrapper = switch(requestParam.getQueryType()){
            case 0 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getPopularFlag, FlagEnum.TRUE.code());
            case 1 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.A_E.getSpells());
            case 2 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.F_J.getSpells());
            case 3 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.K_O.getSpells());
            case 4 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.P_T.getSpells());
            case 5 -> Wrappers.lambdaQuery(RegionDO.class)
                    .in(RegionDO::getInitial, RegionStationQueryTypeEnum.U_Z.getSpells());
            default -> throw new ClientException("查询失败，请检查查询参数是否正确");
        };
        return safeGetRegionStation(
                key,
                ()->{
                    List<RegionDO>regionList = regionMapper.selectList(queryWrapper);
                    return JSON.toJSONString(BeanUtil.convert(regionList, RegionStationQueryRespDTO.class));
                },
                requestParam.getName()
        );
    }

    @Override
    public List<StationQueryRespDTO> listAllStation() {
        return distributedCache.safeGet(
            STATION_ALL,
            List.class,
            () -> BeanUtil.convert(stationMapper.selectList(Wrappers.emptyWrapper()), StationQueryRespDTO.class),
            ADVANCE_TICKET_DAY,
            TimeUnit.DAYS
        );
    }

    private List<RegionStationQueryRespDTO> safeGetRegionStation(final String key, CacheLoader<String> loader, String param){
        List<RegionStationQueryRespDTO> result;
        // 如果缓存中有该数据
        if(CollUtil.isNotEmpty(result = JSON.parseArray(distributedCache.get(key, String.class), RegionStationQueryRespDTO.class))){
            return result;
        }

        String localKey = String.format(LOCK_QUERY_REGION_STATION_LIST, param);

        RLock lock = redissonClient.getLock(localKey);
        lock.lock();
        try{
            // 双重锁，防止在上锁之前缓存已经加载而无意义上锁行为，防止系统资源浪费
            if(CollUtil.isEmpty(result = JSON.parseArray(distributedCache.get(key, String.class), RegionStationQueryRespDTO.class))){
                if(CollUtil.isEmpty(result = loadAndSet(key, loader))){
                    return Collections.emptyList();
                }
            }
        }finally {
            lock.unlock();
        }
        return result;
    }

    private List<RegionStationQueryRespDTO> loadAndSet(final String key, CacheLoader<String> loader){
        String result = loader.load();
        if(CacheUtil.isNullOrBlank(result)){
            return Collections.emptyList();
        }
        List<RegionStationQueryRespDTO> regionStationQueryRespDTOList = JSON.parseArray(result, RegionStationQueryRespDTO.class);
        // 放入缓存
        distributedCache.put(key, result, ADVANCE_TICKET_DAY, TimeUnit.DAYS);
        return regionStationQueryRespDTOList;
    }
}
