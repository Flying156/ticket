package org.learn.index12306.framework.starter.database.handler;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.learn.index12306.framework.starter.distributedid.toolkit.SnowflakeIdUtil;

/**
 * @author Milk
 * @version 2023/9/23 23:03
 */
public class CustomerIdHandler implements IdentifierGenerator {
    @Override
    public Number nextId(Object entity) {
        return SnowflakeIdUtil.nextId();
    }
}
