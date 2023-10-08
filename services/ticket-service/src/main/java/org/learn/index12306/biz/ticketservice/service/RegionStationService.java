package org.learn.index12306.biz.ticketservice.service;

import org.learn.index12306.biz.ticketservice.dto.req.RegionStationQueryReqDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.RegionStationQueryRespDTO;
import org.learn.index12306.biz.ticketservice.dto.resp.StationQueryRespDTO;

import java.util.List;

/**
 * 地区以及车站接口层
 *
 * @author Milk
 * @version 2023/10/7 16:36
 */
public interface RegionStationService {


    /**
     * 查询车站&城市站点集合信息
     *
     * @param requestParam 车站&站点查询参数
     * @return 车站&站点返回数据集合
     */
    List<RegionStationQueryRespDTO> listRegionStation(RegionStationQueryReqDTO requestParam);

    /**
     * 查询所有的站点信息
     *
     * @return 站点信息
     */
    List<StationQueryRespDTO> listAllStation();
}
