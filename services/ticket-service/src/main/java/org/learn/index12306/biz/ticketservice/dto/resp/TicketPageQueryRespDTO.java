package org.learn.index12306.biz.ticketservice.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.learn.index12306.biz.ticketservice.dto.domain.TicketListDTO;

import java.util.List;

/**
 * 车票分页查询响应参数
 *
 * @author Milk
 * @version 2023/10/8 15:31
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketPageQueryRespDTO {

    /**
     * 车次集合
     */
    private List<TicketListDTO> trainList;

    /**
     * 车次类型： D-动车 Z-直达 复兴号等
     */
    private List<Integer> trainBrandList;

    /**
     * 出发站点集合
     */
    private List<String> departureStationList;

    /**
     * 到达站点集合
     */
    private List<String> arrivalStationList;

    /**
     * 车次席别
     */
    private List<Integer> seatClassTypeList;
}
