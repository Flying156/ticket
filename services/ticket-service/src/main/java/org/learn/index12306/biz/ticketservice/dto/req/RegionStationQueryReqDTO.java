package org.learn.index12306.biz.ticketservice.dto.req;

import lombok.Data;

/**
 * 地区&站点查询请求入参
 *
 * @author Milk
 * @version 2023/10/7 17:00
 */
@Data
public class RegionStationQueryReqDTO {

    /**
     * 查询方式
     */
    private Integer queryType;

    /**
     * 名称
     */
    private String name;
}
