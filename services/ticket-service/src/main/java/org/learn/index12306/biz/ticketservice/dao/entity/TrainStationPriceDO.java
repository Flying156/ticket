package org.learn.index12306.biz.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.learn.index12306.framework.starter.database.base.BaseDO;

/**
 * 列车车站价格实体
 *
 * @author Milk
 * @version 2023/10/7 14:28
 */
@Data
@TableName("t_train_station_price")
public class TrainStationPriceDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 车次 id
     */
    private Long trainId;

    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 离开站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

    /**
     * 车票价格
     */
    private Integer price;

}
