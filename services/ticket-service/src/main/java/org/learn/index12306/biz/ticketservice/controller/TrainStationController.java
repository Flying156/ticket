package org.learn.index12306.biz.ticketservice.controller;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.resp.TrainStationQueryRespDTO;
import org.learn.index12306.biz.ticketservice.service.TrainStationService;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.web.Results;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 列车经停站点控制层
 *
 * @author Milk
 * @version 2023/10/7 16:12
 */
@RestController
@RequiredArgsConstructor
public class TrainStationController {

    private final TrainStationService trainStationService;

    /**
     * 根据列车 ID 查询站点信息
     */
    @GetMapping("/api/ticket-service/train-station/query")
    public Result<List<TrainStationQueryRespDTO>> listTrainStationQuery(String trainId){
        return Results.success(trainStationService.listTrainStationQuery(trainId));
    }

}
