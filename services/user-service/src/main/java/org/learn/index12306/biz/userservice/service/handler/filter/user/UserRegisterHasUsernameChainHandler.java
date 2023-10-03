package org.learn.index12306.biz.userservice.service.handler.filter.user;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.userservice.common.enums.UserRegisterErrorCodeEnum;
import org.learn.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.learn.index12306.biz.userservice.service.UserLoginService;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.springframework.stereotype.Component;

/**
 * 用户注册用户名唯一检验
 *
 * @author Milk
 * @version 2023/10/2 13:57
 */
@Component
@RequiredArgsConstructor
public class UserRegisterHasUsernameChainHandler implements UserRegisterCreateChainFilter<UserRegisterReqDTO>{

    private final UserLoginService userLoginService;

    /**
     * 判断用户名是否存在
     * @param requestParam 责任链执行入参
     */
    @Override
    public void handler(UserRegisterReqDTO requestParam) {
        if(!userLoginService.hasUsername(requestParam.getUsername())){
            throw new ClientException(UserRegisterErrorCodeEnum.HAS_USERNAME_NOTNULL);
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
