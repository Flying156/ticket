package org.learn.index12306.framework.starter.idempotent.enums;

/**
 * 幂等验证场景枚举
 *
 * @author Milk
 * @version 2023/10/4 19:26
 */
public enum IdempotentSceneEnum {

    /**
     * 基于 RestAPI 场景验证
     */
    RESTAPI,

    /**
     * 基于 MQ 场景验证
     */
    MQ

}
