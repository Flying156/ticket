package org.learn.index12306.framework.starter.database.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

/**
 * 数据持久层基础属性
 *
 * @author Milk
 * @version 2023/9/22 21:08
 */
@Data
public class BaseDO {


    /**
     * 插入时间
     */
    @TableField(fill = FieldFill.INSERT)
    public Date updateTime;


    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    public Date insertTime;


    /**
     * 是否删除标识
     */
    @TableField(fill = FieldFill.INSERT)
    public Integer delFlag;

}
