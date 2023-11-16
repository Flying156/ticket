package org.learn.index12306.biz.payservice.service.payId;

import lombok.RequiredArgsConstructor;
import org.learn.index12306.framework.statrer.cache.DistributedCache;
import org.learn.index12306.framework.statrer.cache.StringRedisTemplateProxy;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Milk
 * @version 2023/11/9 22:00
 */
@Component
@RequiredArgsConstructor
public class PayIdGeneratorManager implements InitializingBean {

    private final RedissonClient redissonClient;
    private final DistributedCache distributedCache;
    private static DistributedIdGenerator DISTRIBUTED_ID_GENERATOR;

    /**
     * 生成支付全局唯一流水号
     *
     * @param orderSn 订单号
     * @return 支付流水号
     */
    public static String generateId(String orderSn){
        return DISTRIBUTED_ID_GENERATOR.generateId() + orderSn.substring(orderSn.length() - 6);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String LOCK_KEY = "distributed_pay_id_generator_lock_key";
        RLock lock = redissonClient.getLock(LOCK_KEY);
        lock.lock();
        try{
            StringRedisTemplate instance = (StringRedisTemplate) distributedCache.getInstance();
            String DISTRIBUTED_ID_GENERATOR_KEY = "distributed_pay_id_generator_config";
            long incremented = Optional.ofNullable(instance.opsForValue().increment(DISTRIBUTED_ID_GENERATOR_KEY)).orElse(0L);
            int NODE_MAX = 32;
            if(incremented > NODE_MAX){
                incremented = 0;
                instance.opsForValue().set(DISTRIBUTED_ID_GENERATOR_KEY, "0");
            }
            DISTRIBUTED_ID_GENERATOR = new DistributedIdGenerator(incremented);
        }finally {
            lock.unlock();
        }
    }
}
