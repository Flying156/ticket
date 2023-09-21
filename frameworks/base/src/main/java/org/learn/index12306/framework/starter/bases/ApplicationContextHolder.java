package org.learn.index12306.framework.starter.bases;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 在上下文中查找 Bean
 *
 * @author Milk
 * @version 2023/9/21 18:17
 */
public class ApplicationContextHolder implements ApplicationContextAware {

    private static ApplicationContext CONTEXT;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CONTEXT = applicationContext;
    }

    /**
     * 在 IOC 容器中按类型查找
     */
    public static<T> T getBean(Class<T> clazz){
        return CONTEXT.getBean(clazz);
    }

    /**
     * 在 IOC 容器中按名称查找
     */
    public static Object getBean(String name){
        return CONTEXT.getBean(name);
    }

    /**
     * 在 IOC 容器中按名称和类型查找
     */
    public static <T> T getBean(String name, Class<T> clazz){
        return CONTEXT.getBean(name, clazz);
    }

    /**
     * 在 IOC 容器中按照类型获得 Bean 集合
     */
    public static <T> Map<String, T> getBeansOfType(Class<T> clazz){
        return CONTEXT.getBeansOfType(clazz);
    }

    /**
     * 检查 Bean 是否有可以识别的注解
     */
    public static <T extends Annotation> T findAnnotationOnBean(String beanName, Class<T> clazz){
        return CONTEXT.findAnnotationOnBean(beanName, clazz);
    }


    public static ApplicationContext getInstance(){
        return CONTEXT;
    }
}
