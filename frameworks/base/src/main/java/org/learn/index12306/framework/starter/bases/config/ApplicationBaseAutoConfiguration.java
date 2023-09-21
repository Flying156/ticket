package org.learn.index12306.framework.starter.bases.config;

import org.learn.index12306.framework.starter.bases.ApplicationContextHolder;
import org.learn.index12306.framework.starter.bases.init.ApplicationContentPostProcessor;
import org.learn.index12306.framework.starter.bases.safa.FastJsonSafeMode;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Bean 自动装配, 自定义 Starter
 *
 * @author Milk
 * @version 2023/9/21 17:33
 */
public class ApplicationBaseAutoConfiguration {

    /**
     * 根据情况扫描包内的所有 Bean
     */
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder applicationContextHolder(){
        return new ApplicationContextHolder();
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationContentPostProcessor applicationContentPostProcessor(ApplicationContext applicationContext){
        return new ApplicationContentPostProcessor(applicationContext);
    }


    /**
     * 根据属性是否开启 FastJson 安全模式
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "framework.fastjson.safa-mode", havingValue = "true")
    public FastJsonSafeMode fastJsonSafeMode(){
        return new FastJsonSafeMode();
    }

}
