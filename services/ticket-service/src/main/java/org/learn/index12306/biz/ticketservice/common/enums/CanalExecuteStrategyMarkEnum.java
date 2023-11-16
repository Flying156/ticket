package org.learn.index12306.biz.ticketservice.common.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

/**
 * Canal 执行策略标记枚举
 *
 * @author Milk
 * @version 2023/11/4 14:23
 */
@RequiredArgsConstructor
public enum CanalExecuteStrategyMarkEnum {

    /**
     * 座位表
     */
    T_SEAT("t_seat", null),

    /**
     * 订单表
     */
    T_ORDER("t_order", "^t_order_([0-9]+|1[0-6])");

    @Getter
    private final String actualTable;

    @Getter
    private final String patternMatchTable;

    public static boolean isPatternMatch(String tableName){
        return Arrays.stream(CanalExecuteStrategyMarkEnum.values())
                .anyMatch(each -> StrUtil.isNotBlank(each.getPatternMatchTable()));
    }

    public static String getPatternMatch(String tableName){
        return Arrays.stream(CanalExecuteStrategyMarkEnum.values())
                .filter(each -> Objects.equals(tableName, each.getActualTable()))
                .findFirst()
                .map(CanalExecuteStrategyMarkEnum::getPatternMatchTable)
                .orElse(null);
    }
}
