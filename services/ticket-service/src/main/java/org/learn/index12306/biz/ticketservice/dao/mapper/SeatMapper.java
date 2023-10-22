package org.learn.index12306.biz.ticketservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.learn.index12306.biz.ticketservice.dao.entity.SeatDO;

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
}
