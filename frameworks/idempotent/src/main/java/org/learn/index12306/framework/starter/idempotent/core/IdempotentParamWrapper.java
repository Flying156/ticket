package org.learn.index12306.framework.starter.idempotent.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.aspectj.lang.ProceedingJoinPoint;
import org.learn.index12306.framework.starter.idempotent.annotation.Idempotent;
import org.learn.index12306.framework.starter.idempotent.enums.IdempotentTypeEnum;

/**
 * 幂等参数包装
 *
 * @author Milk
 * @version 2023/10/5 19:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IdempotentParamWrapper {

    /**
     * 幂等注解
     */
    private Idempotent idempotent;

    /**
     * AOP 处理连接点
     */
    private ProceedingJoinPoint joinPoint;

    /**
     * 锁标识，{@link IdempotentTypeEnum#PARAM}
     */
    private String lockKey;

}
