package org.learn.index12306.biz.ticketservice.dto.resp;

import lombok.Data;

/**
 * 站点分页查询参数
 *
 * @author Milk
 * @version 2023/10/7 19:45
 */
@Data
public class StationQueryRespDTO {

    /**
     * 名称
     */
    private String name;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 拼音
     */
    private String spell;

    /**
     * 城市名称
     */
    private String regionName;

}
