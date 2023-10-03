package org.learn.index12306.biz.userservice.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.learn.index12306.biz.userservice.dao.entity.UserPhoneDO;

/**
 * 用户手机号持久层
 *
 * @author Milk
 * @version 2023/10/1 19:55
 */
public interface UserPhoneMapper extends BaseMapper<UserPhoneDO> {


    /**
     * 注销用户
     *
     * @param userPhoneDO 注销用户入参
     */
    void deletionUser(UserPhoneDO userPhoneDO);
}
