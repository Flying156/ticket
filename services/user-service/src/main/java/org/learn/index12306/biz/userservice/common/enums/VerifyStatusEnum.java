package org.learn.index12306.biz.userservice.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户注册错误码枚举
 *
 * @author Milk
 * @version 2023/10/3 14:51
 */
@Getter
@AllArgsConstructor
public enum VerifyStatusEnum {

    /**
     * 未审核
     */
    UNREVIEWED(0),

    /**
     * 已审核
     */
    REVIEWED(1);

    private final int code;
}