package org.learn.index12306.framework.starter.designpattern.config;

import org.learn.index12306.framework.starter.bases.config.ApplicationBaseAutoConfiguration;
import org.learn.index12306.framework.starter.designpattern.chain.AbstractChainContext;
import org.learn.index12306.framework.starter.designpattern.strategy.AbstractStrategyChoose;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 设计模式自动装配
 *
 * @author Milk
 * @version 2023/9/29 16:40
 */
@ImportAutoConfiguration(ApplicationBaseAutoConfiguration.class)
public class DesignPatternAutoConfiguration {

    /**
     * 策略模式选择器
     */
    @Bean
    public AbstractStrategyChoose abstractStrategyChoose(){
        return new AbstractStrategyChoose();
    }

    /**
     * 责任链上下文
     */
    @Bean
    public AbstractChainContext abstractChainContext(){
        return new AbstractChainContext();
    }

}
