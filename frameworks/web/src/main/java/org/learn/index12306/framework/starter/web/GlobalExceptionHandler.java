package org.learn.index12306.framework.starter.web;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.learn.index12306.framework.starter.convention.errorCode.BaseErrorCode;
import org.learn.index12306.framework.starter.convention.exception.AbstractException;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

/**
 * 全局异常拦截处理器
 *
 * @author Milk
 * @version 2023/9/24 16:26
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 拦截应用内抛出的异常
     */
    @ExceptionHandler(value = {AbstractException.class})
    public Result abstractException(HttpServletRequest request, AbstractException ex){
        if(ex.getCause() != null){
            log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request),ex.toString(), ex.getCause());
            return Results.failure(ex);
        }
        log.error("[{}] {} [ex] {}", request.getMethod(), request.getRequestURL().toString(), ex.toString());
        return Results.failure(ex);

    }

    /**
     * 拦截参数校验失败的异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public Result methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException ex){
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstFieldError = CollectionUtil.getFirst(bindingResult.getFieldErrors());
        String exceptionStr = Optional.ofNullable(firstFieldError)
                        .map(FieldError::getDefaultMessage)
                        .orElse(StrUtil.EMPTY);
        log.error("[{}] {} [ex] {}", request.getMethod(), getUrl(request), exceptionStr);
        return Results.failure(BaseErrorCode.CLIENT_ERROR.code(), exceptionStr);
    }


    @ExceptionHandler(value = Throwable.class)
    public Result defaultErrorHandler(HttpServletRequest request, Throwable throwable){
        log.error("[{}] {}", request.getMethod(), getUrl(request), throwable);
        return Results.failure();
    }


    private String getUrl(HttpServletRequest request){
        if(StrUtil.isEmpty(request.getQueryString())){
            return request.getRequestURL().toString();
        }
        return request.getRequestURL() + "?" + request.getQueryString();
    }
}
