package org.learn.index12306.framework.starter.log.config;

import org.learn.index12306.framework.starter.log.annotation.ILog;
import org.learn.index12306.framework.starter.log.core.ILogPrintAspect;
import org.springframework.context.annotation.Bean;

/**
 * 日志类装配
 *
 * @author Milk
 * @version 2023/9/25 21:31
 */
public class LogAutoConfiguration {

    /**
     * {@link ILog} 日志打印 AOP 切面
     */
    @Bean
    public ILogPrintAspect iLogPrintAspect(){
        return new ILogPrintAspect();
    }
}
