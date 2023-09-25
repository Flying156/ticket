package org.learn.index12306.framework.starter.log.core;

import lombok.Data;

/**
 * ILog 日志打印实体
 *
 * @author Milk
 * @version 2023/9/25 20:21
 */
@Data
public class ILogPrintDTO {

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 请求入参
     */
    private Object[] inputParams;

    /**
     * 返回参数
     */
    private Object outputParams;
}
