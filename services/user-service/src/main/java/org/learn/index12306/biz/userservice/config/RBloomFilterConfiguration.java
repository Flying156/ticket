package org.learn.index12306.biz.userservice.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 布隆过滤器配置
 *
 * @author Milk
 * @version 2023/10/1 14:45
 */
@Configuration
@EnableConfigurationProperties(UserRegisterBloomFilterProperties.class)
public class RBloomFilterConfiguration {

    /**
     * 初始化布隆过滤器
     */
    @Bean
    public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(RedissonClient redissonClient, UserRegisterBloomFilterProperties userRegisterBloomFilterProperties){
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(userRegisterBloomFilterProperties.getName());
        cachePenetrationBloomFilter.tryInit(userRegisterBloomFilterProperties.getExpectedInsertions(), userRegisterBloomFilterProperties.getFalseProbability());
        return cachePenetrationBloomFilter;
    }

}
