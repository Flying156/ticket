package org.learn.index12306.biz.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.learn.index12306.biz.userservice.dao.entity.UserMailDO;

/**
 * 用户邮箱持久层
 *
 * @author Milk
 * @version 2023/10/1 19:55
 */
public interface UserMailMapper extends BaseMapper<UserMailDO> {

    /**
     * 注销用户
     *
     * @param userMailDO 注销用户入参
     */
    void deletionUser(UserMailDO userMailDO);
}
