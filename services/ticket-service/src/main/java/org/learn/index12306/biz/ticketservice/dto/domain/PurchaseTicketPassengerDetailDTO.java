package org.learn.index12306.biz.ticketservice.dto.domain;

import lombok.Data;

/**
 * 乘车人详细信息
 *
 * @author Milk
 * @version 2023/10/12 19:22
 */
@Data
public class PurchaseTicketPassengerDetailDTO {

    /**
     * 乘车人 ID
     */
    private String passengerId;

    /**
     * 座位类型
     */
    private Integer seatType;
}
