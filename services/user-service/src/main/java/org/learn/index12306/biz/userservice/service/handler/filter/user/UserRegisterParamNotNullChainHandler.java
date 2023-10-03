package org.learn.index12306.biz.userservice.service.handler.filter.user;

import org.learn.index12306.biz.userservice.common.enums.UserRegisterErrorCodeEnum;
import org.learn.index12306.biz.userservice.dto.req.UserRegisterReqDTO;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 用户注册参数必填检验
 *
 * @author Milk
 * @version 2023/10/2 14:11
 */
@Component
public class UserRegisterParamNotNullChainHandler implements UserRegisterCreateChainFilter<UserRegisterReqDTO>{

    /**
     * 判断用户注册时的参数是否为空
     * @param requestParam 责任链执行入参
     */
    @Override
    public void handler(UserRegisterReqDTO requestParam) {
        if (Objects.isNull(requestParam.getUsername())) {
            throw new ClientException(UserRegisterErrorCodeEnum.USER_NAME_NOTNULL);
        } else if (Objects.isNull(requestParam.getPassword())) {
            throw new ClientException(UserRegisterErrorCodeEnum.PASSWORD_NOTNULL);
        } else if (Objects.isNull(requestParam.getPhone())) {
            throw new ClientException(UserRegisterErrorCodeEnum.PHONE_NOTNULL);
        } else if (Objects.isNull(requestParam.getIdType())) {
            throw new ClientException(UserRegisterErrorCodeEnum.ID_TYPE_NOTNULL);
        } else if (Objects.isNull(requestParam.getIdCard())) {
            throw new ClientException(UserRegisterErrorCodeEnum.ID_CARD_NOTNULL);
        } else if (Objects.isNull(requestParam.getMail())) {
            throw new ClientException(UserRegisterErrorCodeEnum.MAIL_NOTNULL);
        } else if (Objects.isNull(requestParam.getRealName())) {
            throw new ClientException(UserRegisterErrorCodeEnum.REAL_NAME_NOTNULL);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
