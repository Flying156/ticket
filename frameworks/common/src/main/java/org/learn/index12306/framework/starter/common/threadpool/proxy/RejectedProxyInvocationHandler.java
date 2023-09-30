package org.learn.index12306.framework.starter.common.threadpool.proxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池拒绝策略代理执行器
 * 使用 JDK动态代理，
 * <p>JDK的动态代理依靠接口实现，如果有些类并没有接口实现，则不能使用JDK代理</p>
 * @author Milk
 * @version 2023/9/30 16:52
 */
@Slf4j
@AllArgsConstructor
public class RejectedProxyInvocationHandler implements InvocationHandler {

    /**
     * 被代理的对象
     */
    private final Object target;

    /**
     * 线程创建添加时被拒绝的次数
     */
    private final AtomicLong rejectCount;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        rejectCount.incrementAndGet();
        try{
            log.error("线程池执行拒绝策略, 此处模拟报警...");
            return method.invoke(target, args);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }
    }
}
