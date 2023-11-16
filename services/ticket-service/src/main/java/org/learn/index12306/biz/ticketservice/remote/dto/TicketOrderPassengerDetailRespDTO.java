package org.learn.index12306.biz.ticketservice.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 车票订单详情返回参数
 *
 * @author Milk
 * @version 2023/11/4 22:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketOrderPassengerDetailRespDTO {

    /**
     * 用户 id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 座位类型
     */
    private Integer seatType;

    /**
     * 座位号
     */
    private String  seatNumber;

    /**
     * 车厢号
     */
    private String carriageNumber;

    /**
     * 证件号
     */
    private String idCard;

    /**
     * 证件类型
     */
    private Integer idType;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 车票类型 0：成人 1：儿童 2：学生 3：残疾军人
     */
    private Integer ticketType;

    /**
     * 订单金额
     */
    private Integer amount;

    /**
     * 车票状态
     */
    private Integer status;

}
