package org.learn.index12306.framework.statrer.cache.config;

import lombok.AllArgsConstructor;
import org.learn.index12306.framework.statrer.cache.RedisKeySerializer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 缓存配置自动装配
 *
 * @author Milk
 * @version  2023/9/19 19:13
 */

@AllArgsConstructor
@EnableConfigurationProperties({RedisDistributedProperties.class, BloomFilterPenetrateProperties.class})
public class CacheAutoConfiguration {

    private final RedisDistributedProperties redisDistributedProperties;


    /**
     * 创建 Redis Key 序列化器， 自定义 Key Prefix
     */
    @Bean
    public RedisKeySerializer redisKeySerializer(){
        String prefix = redisDistributedProperties.getPrefix();
        String prefixCharset = redisDistributedProperties.getPrefixCharset();
        return new RedisKeySerializer(prefix, prefixCharset);
    }

}
