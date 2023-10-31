package org.learn.index12306.biz.ticketservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.learn.index12306.biz.ticketservice.dao.entity.SeatDO;
import org.learn.index12306.biz.ticketservice.dto.domain.SeatTypeCountDTO;

import java.util.List;

/**
 * 座位持久层
 *
 * @author Milk
 * @version 2023/10/7 14:57
 */
public interface SeatMapper extends BaseMapper<SeatDO> {

    /**
     * 获取车厢余票集合
     */
    List<Integer> listSeatRemainingTicket(@Param("seatDO") SeatDO seatDO, @Param("trainCarriageList") List<String> trainCarriageList);

    /**
     * 根据座位类型列出座位数量
     */
    List<SeatTypeCountDTO> listSeatTypeCount(@Param("trainId") Long trainId, @Param("startStation") String startStation, @Param("endStation") String endStation, @Param("seatTypes")  List<Integer> seatTypes);
}
