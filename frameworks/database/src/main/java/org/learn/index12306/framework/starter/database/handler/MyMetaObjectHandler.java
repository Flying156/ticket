package org.learn.index12306.framework.starter.database.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.learn.index12306.framework.starter.common.enums.DelEnum;

import java.util.Date;

/**
 * 新增数据或修改数据时插入时间
 *
 * @author Milk
 * @version 2023/9/22 20:54
 */
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增数据时自动插入
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
        this.strictInsertFill(metaObject, "delFlag", Integer.class, DelEnum.NORMAL.code());
    }

    /**
     * 修改数据时自动插入
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "updateTime", Date.class, new Date());
    }
}
