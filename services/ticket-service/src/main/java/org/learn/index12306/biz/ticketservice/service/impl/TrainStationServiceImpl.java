package org.learn.index12306.biz.ticketservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dao.entity.TrainStationDO;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainStationMapper;
import org.learn.index12306.biz.ticketservice.dto.domain.RouteDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.TrainStationQueryRespDTO;
import org.learn.index12306.biz.ticketservice.service.TrainStationService;
import org.learn.index12306.biz.ticketservice.toolkit.StationCalculateUtil;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 列车站点接口层实现
 *
 * @author Milk
 * @version 2023/10/7 16:11
 */
@Service
@RequiredArgsConstructor
public class TrainStationServiceImpl implements TrainStationService {

    private final TrainStationMapper trainStationMapper;

    @Override
    public List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId) {
        LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, trainId);
        List<TrainStationDO> trainStationList = trainStationMapper.selectList(queryWrapper);
        return BeanUtil.convert(trainStationList, TrainStationQueryRespDTO.class);
    }

    @Override
    public List<RouteDTO> listTrainStationRoute(String trainId, String departure, String arrival) {
        LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, trainId)
                .select(TrainStationDO::getDeparture);
        List<TrainStationDO> trainStationDOList = trainStationMapper.selectList(queryWrapper);
        List<String> trainStationAllList = trainStationDOList.stream().map(TrainStationDO::getDeparture).toList();
        return StationCalculateUtil.throughStation(trainStationAllList, departure, arrival);
    }

    @Override
    public List<RouteDTO> listTakeoutTrainStationRoute(String trainId, String departure, String arrival) {
        LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                .eq(TrainStationDO::getTrainId, trainId)
                .select(TrainStationDO::getDeparture);
        List<TrainStationDO> trainStationDOList = trainStationMapper.selectList(queryWrapper);
        List<String> trainStationAllList = trainStationDOList.stream().map(TrainStationDO::getDeparture).collect(Collectors.toList());
        return StationCalculateUtil.takeoutStation(trainStationAllList, departure, arrival);
    }
}
