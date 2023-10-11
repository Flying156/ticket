package org.learn.index12306.biz.ticketservice.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 站点路线实体
 *
 * @author Milk
 * @version 2023/10/10 16:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDTO {

    /**
     * 出发站点
     */
    private String startStation;

    /**
     * 目的站点
     */
    private String endStation;
}
