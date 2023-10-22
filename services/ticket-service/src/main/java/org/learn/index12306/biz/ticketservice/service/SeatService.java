package org.learn.index12306.biz.ticketservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.learn.index12306.biz.ticketservice.dao.entity.SeatDO;
import org.learn.index12306.biz.ticketservice.service.handler.ticket.dto.TrainPurchaseTicketRespDTO;

import java.util.List;

/**
 * 座位接口
 *
 * @author Milk
 * @version 2023/10/13 20:47
 */
public interface SeatService extends IService<SeatDO> {

    /**
     * 查询列车有余票的车厢号集合
     *
     * @param trainId   列车 ID
     * @param seatType  座位类型
     * @param departure 出发站点
     * @param arrival   抵达站点
     * @return  车厢列表
     */
    List<String> listUsableCarriageNumber(String trainId, Integer seatType, String departure, String arrival);


    /**
     * 查询列车有余票的车厢的余票集合
     *
     * @param trainId           列车 ID
     * @param departure         出发站点
     * @param arrival           抵达站点
     * @param trainCarriageList 车厢编号集合
     * @return 余票数量列表
     */
    List<Integer> listSeatRemainingTicket(String trainId, String departure, String arrival, List<String> trainCarriageList);

    /**
     * 列出可用的座位
     *
     * @param trainId        列车 ID
     * @param carriageNumber 车厢 ID
     * @param seatType       座位类型
     * @param departure      出发站点
     * @param arrival        抵达站点
     * @return               可用的座位集合
     */
    List<String> listAvailableSeat(String trainId, String carriageNumber, Integer seatType, String departure, String arrival);


    /**
     * 锁定选中以及沿途车票状态
     *
     * @param trainId                     列车 ID
     * @param departure                   出发站
     * @param arrival                     到达站
     * @param trainPurchaseTicketRespList 乘车人以及座位信息
     */
    void lockSeat(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketRespList);

    /**
     * 解锁选中以及沿途车票状态
     *
     * @param trainId                    列车 ID
     * @param departure                  出发站
     * @param arrival                    到达站
     * @param trainPurchaseTicketResults 乘车人以及座位信息
     */
    void unlock(String trainId, String departure, String arrival, List<TrainPurchaseTicketRespDTO> trainPurchaseTicketResults);
}
