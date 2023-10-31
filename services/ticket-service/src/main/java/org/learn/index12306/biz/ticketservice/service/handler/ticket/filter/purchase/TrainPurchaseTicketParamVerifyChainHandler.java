package org.learn.index12306.biz.ticketservice.service.handler.ticket.filter.purchase;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dao.entity.TrainDO;
import org.learn.index12306.biz.ticketservice.dao.entity.TrainStationDO;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainMapper;
import org.learn.index12306.biz.ticketservice.dao.mapper.TrainStationMapper;
import org.learn.index12306.biz.ticketservice.dto.req.PurchaseTicketReqDTO;
import org.learn.index12306.framework.starter.common.toolkit.EnvironmentUtil;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.learn.index12306.biz.ticketservice.common.constant.Index12306Constant.ADVANCE_TICKET_DAY;
import static org.learn.index12306.biz.ticketservice.common.constant.RedisKeyConstant.*;

/**
 * 购票流程过滤器之验证参数是否有效
 *
 * @author Milk
 * @version 2023/10/12 19:47
 */
@Component
@RequiredArgsConstructor
public class TrainPurchaseTicketParamVerifyChainHandler implements TrainTicketPurchaseChainFilter<PurchaseTicketReqDTO> {

    private final TrainMapper trainMapper;
    private final TrainStationMapper trainStationMapper;
    private final DistributedCache distributedCache;

    @Override
    public void handler(PurchaseTicketReqDTO requestParam) {
        TrainDO trainDO = distributedCache.safeGet(
                TRAIN_INFO + requestParam.getTrainId(),
                TrainDO.class,
                () -> trainMapper.selectById(requestParam.getTrainId()),
                ADVANCE_TICKET_DAY,
                TimeUnit.DAYS
        );
        if(Objects.isNull(trainDO)){
            // 如果按照严谨逻辑，类似异常应该记录当前用户的 userid 并发送到风控中心
            // 如果一段时间有过几次的异常，直接封号处理。下述异常同理
            throw new ClientException("请检查列车是否存在");
        }
        // 便于开发，没有添加发售时间
        if(!EnvironmentUtil.isDevEnvironment()){
            // 查询车次是否已经发售
            if (new Date().before(trainDO.getSaleTime())) {
                throw new ClientException("列车车次暂未发售");
            }
            // 查询车次是否在有效期内
            if (new Date().after(trainDO.getDepartureTime())) {
                throw new ClientException("列车车次已出发禁止购票");
            }
        }
        // 判断车站是否存在该班次，顺序是否正确
        String trainStationStopoverDetailStr =  distributedCache.safeGet(
                TRAIN_STATION_STOPOVER_DETAIL + requestParam.getTrainId(),
                String.class,
                () ->{
                    LambdaQueryWrapper<TrainStationDO> queryWrapper = Wrappers.lambdaQuery(TrainStationDO.class)
                            .eq(TrainStationDO::getTrainId, requestParam.getTrainId())
                            .select(TrainStationDO::getDeparture);
                    List<TrainStationDO> actualTrainStationList = trainStationMapper.selectList(queryWrapper);
                    return CollUtil.isNotEmpty(actualTrainStationList) ? JSON.toJSONString(actualTrainStationList) : null;
                },
                ADVANCE_TICKET_DAY,
                TimeUnit.DAYS
        );
        List<TrainStationDO> trainStationList = JSON.parseArray(trainStationStopoverDetailStr, TrainStationDO.class);
        boolean validateStation = validateStation(
                trainStationList.stream().map(TrainStationDO::getDeparture).toList(),
                requestParam.getDeparture(),
                requestParam.getArrival()
        );
        if(!validateStation){
            throw new ClientException("列车车站数据错误");
        }

    }

    @Override
    public int getOrder() {
        return 10;
    }


    private boolean validateStation(List<String> stationList, String startStation, String endStation){
        int index1 = stationList.indexOf(startStation);
        int index2 = stationList.indexOf(endStation);
        if(index2 == -1 || index1 == -1){
            return false;
        }
        return index2 >= index1;
    }
}
