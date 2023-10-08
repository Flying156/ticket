package org.learn.index12306.biz.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.learn.index12306.framework.starter.database.base.BaseDO;

/**
 * 车厢实体
 *
 * @author Milk
 * @version 2023/10/6 22:04
 */
@Data
@TableName("t_carriage")
public class CarriageDO extends BaseDO {


    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 列车id
     */
    private Long trainId;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 车厢类型
     */
    private Integer carriageType;

    /**
     * 座位数
     */
    private Integer seatCount;



}
