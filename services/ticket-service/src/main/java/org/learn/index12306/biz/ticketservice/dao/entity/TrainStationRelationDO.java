package org.learn.index12306.biz.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.learn.index12306.framework.starter.database.base.BaseDO;

import java.util.Date;

/**
 * 列车车站关系实体
 *
 * @author Milk
 * @version 2023/10/7 14:37
 */
@Data
@TableName("t_train_station_relation")
public class TrainStationRelationDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 车次 id
     */
    private Long trainId;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 始发站标识
     */
    private Boolean departureFlag;

    /**
     * 终点站标识
     */
    private Boolean arrivalFlag;

    /**
     * 出发城市
     */
    private String startRegion;

    /**
     * 终点城市
     */
    private String endRegion;

    /**
     * 出发时间
     */
    private Date departureTime;

    /**
     * 到站时间
     */
    private Date arrivalTime;

}

