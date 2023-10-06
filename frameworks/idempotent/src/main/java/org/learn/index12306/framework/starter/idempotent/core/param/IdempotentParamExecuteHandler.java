package org.learn.index12306.framework.starter.idempotent.core.param;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.learn.index12306.framework.starter.convention.exception.ClientException;
import org.learn.index12306.framework.starter.idempotent.core.AbstractIdempotentExecuteHandler;
import org.learn.index12306.framework.starter.idempotent.core.IdempotentContext;
import org.learn.index12306.framework.starter.idempotent.core.IdempotentParamWrapper;
import org.learn.index12306.framework.starter.user.core.UserContext;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 基于方法参数验证请求幂等性,使用分布式锁解决问题
 *
 * @author Milk
 * @version 2023/10/6 14:17
 */
@RequiredArgsConstructor
public final class IdempotentParamExecuteHandler extends AbstractIdempotentExecuteHandler implements IdempotentParamService{

    private final RedissonClient redissonClient;

    private final static String LOCK = "lock:param:restAPI";


    @Override
    protected IdempotentParamWrapper buildWrapper(ProceedingJoinPoint joinPoint) {
        String lockKey = String.format("idempotent:path:%s:currentUserId:%s:md5:%s", getServletPath(), getCurrentUserId(), calcArgsMD5(joinPoint));
        return IdempotentParamWrapper.builder().lockKey(lockKey).joinPoint(joinPoint).build();
    }


    /**
     * @return joinPoint md5
     */
    private String calcArgsMD5(ProceedingJoinPoint joinPoint) {
        return DigestUtil.md5Hex(JSON.toJSONBytes(joinPoint.getArgs()));
    }

    /**
     * @return 当前操作用户 ID
     */
    private String getCurrentUserId() {
        String userId = UserContext.getUserId();
        if(StrUtil.isBlank(userId)){
            throw new ClientException("用户ID获取失败，请登录");
        }
        return userId;
    }

    /**
     *  @return 获取当前线程上下文 ServletPath
     */
    private String getServletPath() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return sra.getRequest().getServletPath();
    }

    @Override
    public void handler(IdempotentParamWrapper wrapper) {
        String localKey = wrapper.getLockKey();
        RLock lock = redissonClient.getLock(localKey);
        if(!lock.tryLock()){
            throw new ClientException(wrapper.getIdempotent().message());
        }
        IdempotentContext.put(LOCK, lock);
    }

    @Override
    public void postProcessing() {
        RLock lock = null;
        try{
            lock = (RLock) IdempotentContext.getKey(LOCK);
        }finally{
            if(lock != null) {
                lock.unlock();
            }
        }
    }


}
