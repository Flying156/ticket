package org.learn.index12306.biz.gatewayservice.config;

import lombok.Data;

import java.util.List;

/**
 * 过滤器配置
 *
 * @author Milk
 * @version 2023/10/3 18:03
 */
@Data
public class Config {

    /**
     * 黑名单前置路径
     */
    private List<String> blackPathPre;
}
