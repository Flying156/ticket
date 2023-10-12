package org.learn.index12306.biz.ticketservice.dto.req;

import lombok.Data;
import org.learn.index12306.biz.ticketservice.dto.domain.PurchaseTicketPassengerDetailDTO;

import java.util.List;

/**
 * 购票请求入参
 *
 * @author Milk
 * @version 2023/10/12 19:18
 */
@Data
public class PurchaseTicketReqDTO {

    /**
     * 车次 ID
     */
    private String trainId;

    /**
     * 乘车人
     */
    private List<PurchaseTicketPassengerDetailDTO> passengers;

    /**
     * 选择座位
     */
    private List<String> chooseSeats;

    /**
     * 出发站点
     */
    private String departure;

    /**
     * 到达站点
     */
    private String arrival;

}
