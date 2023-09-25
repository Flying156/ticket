package org.learn.index12306.framework.starter.log.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.SystemClock;
import com.alibaba.fastjson2.JSON;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.learn.index12306.framework.starter.log.annotation.ILog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * {@link ILog} 日志打印切面
 *
 * @author Milk
 * @version 2023/9/25 20:30
 */
@Aspect
public class ILogPrintAspect {

    /**
     * 打印类或方法上的 {@link ILog}
     */
    @Around("@annotation(org.learn.index12306.framework.starter.log.annotation.ILog) || @within(org.learn.index12306.framework.starter.log.annotation.ILog)")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = SystemClock.now();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Logger log = LoggerFactory.getLogger(methodSignature.getDeclaringType());
        String beginTime = DateUtil.now();
        Object result = null;
        try{
            result = joinPoint.proceed();
        }finally{
            Method targetMethod = joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
            ILog logAnnotation = Optional.ofNullable(targetMethod.getAnnotation(ILog.class)).orElse(joinPoint.getTarget().getClass().getAnnotation(ILog.class));
            if(logAnnotation != null){
                ILogPrintDTO logPrint = new ILogPrintDTO();
                logPrint.setBeginTime(beginTime);
                if(logAnnotation.input()){
                    logPrint.setInputParams(buildInput(joinPoint));
                }
                if(logAnnotation.output()){
                    logPrint.setOutputParams(result);
                }
                String methodType = "", requestURI = "";
                try{
                    ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                    assert servletRequestAttributes != null;
                    methodType = servletRequestAttributes.getRequest().getMethod();
                    requestURI = servletRequestAttributes.getRequest().getRequestURI();
                }catch (Exception ignored){

                }
                log.info("[{}] {}, executeTime: {}ms, info: {}", methodType, requestURI, SystemClock.now() - startTime, JSON.toJSONString(logPrint));

            }
        }
        return result;
    }

    private Object[] buildInput(ProceedingJoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        Object[] printArgs = new Object[args.length];
        for(int i = 0; i < args.length; i++){
            if((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse){
                continue;
            }
            if(args[i] instanceof byte[]) {
                printArgs[i] = "byte array";
            } else if(args[i] instanceof MultipartFile){
                printArgs[i] = "args";
            }else{
                printArgs[i] = args[i];
            }
        }
        return printArgs;
    }
}
