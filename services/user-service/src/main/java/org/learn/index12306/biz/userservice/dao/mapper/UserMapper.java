package org.learn.index12306.biz.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.learn.index12306.biz.userservice.dao.entity.UserDO;

/**
 * 用户持久层
 *
 * @author Milk
 * @version 2023/10/1 19:54
 */
public interface UserMapper extends BaseMapper<UserDO> {


    /**
     * 注销用户
     *
     * @param userDO 注销用户入参
     */
    void deletionUser(UserDO userDO);
}
