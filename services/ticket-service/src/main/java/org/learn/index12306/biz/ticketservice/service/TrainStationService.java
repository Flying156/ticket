package org.learn.index12306.biz.ticketservice.service;

import org.learn.index12306.biz.ticketservice.dto.resp.TrainStationQueryRespDTO;

import java.util.List;

/**
 * 列车站点接口层
 *
 * @author Milk
 * @version 2023/10/7 16:10
 */
public interface TrainStationService {

    /**
     * 根据列车 id 查询所经站点信息，开车时间，发车时间
     *
     * @param trainId 列车 id
     * @return 列车经停站点信息
     */
    List<TrainStationQueryRespDTO> listTrainStationQuery(String trainId);
}
