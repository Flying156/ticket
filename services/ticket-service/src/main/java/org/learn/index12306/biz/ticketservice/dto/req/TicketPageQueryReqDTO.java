package org.learn.index12306.biz.ticketservice.dto.req;

import lombok.Data;
import org.learn.index12306.framework.starter.convention.page.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 车票查询入参
 *
 * @author Milk
 * @version 2023/10/8 15:25
 */
@Data
public class TicketPageQueryReqDTO extends PageRequest {


    /**
     * 出发地 Code
     */
    private String fromStation;

    /**
     * 目的地 Code
     */
    private String toStation;

    /**
     * 出发时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date departureDate;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 目的站点
     */
    private String arrival;
}
