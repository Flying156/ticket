package org.learn.index12306.framework.starter.idempotent.core.token;

import org.learn.index12306.framework.starter.idempotent.core.IdempotentExecuteHandler;

/**
 * Token 实现幂等组件
 *
 * @author Milk
 * @version 2023/10/5 19:15
 */
public interface IdempotentTokenService extends IdempotentExecuteHandler {

    /**
     * 创建幂等验证Token
     */
    String createToken();
}
