package org.learn.index12306.biz.ticketservice.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 地区实体
 *
 * @author Milk
 * @version 2023/10/7 11:17
 */

@Data
@TableName("t_region")
public class RegionDO {

    /**
     * id
     */
    private Long id;

    /**
     * 地区名称
     */
    private String name;

    /**
     * 地区编码
     */
    private String code;

    /**
     * 地区全名称
     */
    private String fullName;

    /**
     * 地区首字母
     */
    private String initial;

    /**
     * 拼音
     */
    private String spell;

    /**
     * 热门标识
     */
    private Integer popularFlag;

}
