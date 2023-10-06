package org.learn.index12306.framework.starter.idempotent.toolkit;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 日志工具类
 *
 * @author Milk
 * @version 2023/10/6 19:06
 */
public class LogUtil {

    /**
     * 获取 Logger
     */
    public static Logger getLog(ProceedingJoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        return LoggerFactory.getLogger(methodSignature.getDeclaringType());
    }
}
