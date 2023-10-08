package org.learn.index12306.biz.ticketservice.dto.resp;

import lombok.Data;

/**
 * 地区&站点分页查询响应参数
 *
 * @author Milk
 * @version 2023/10/7 17:02
 */
@Data
public class RegionStationQueryRespDTO {

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区拼音
     */
    private String spell;

    /**
     * 编码
     */
    private String code;

}
