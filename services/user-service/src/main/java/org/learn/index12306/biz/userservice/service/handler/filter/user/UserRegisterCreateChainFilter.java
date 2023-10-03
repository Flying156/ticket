package org.learn.index12306.biz.userservice.service.handler.filter.user;

import org.learn.index12306.biz.userservice.common.enums.UserChainMarkEnum;
import org.learn.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.learn.index12306.framework.starter.designpattern.chain.AbstractChainHandler;

/**
 * 用户注册责任链过滤器
 *
 * @author Milk
 * @version 2023/10/2 13:54
 */
public interface UserRegisterCreateChainFilter<T extends UserRegisterReqDTO> extends AbstractChainHandler<UserRegisterReqDTO> {

    @Override
    default String mark(){
        return UserChainMarkEnum.USER_REGISTER_FILTER.name();
    }
}
