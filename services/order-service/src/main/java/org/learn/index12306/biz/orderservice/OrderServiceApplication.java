package org.learn.index12306.biz.orderservice;

import cn.crane4j.spring.boot.annotation.EnableCrane4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * OrderService 启动类
 *
 * @author Milk
 * @version 2023/10/18 22:50
 */
@SpringBootApplication
@MapperScan("org.learn.index12306.biz.orderservice.dao.mapper")
@EnableFeignClients("org.learn.index12306.biz.orderservice.remote")
@EnableCrane4j(enumPackages = "org.learn.index12306.biz.orderservice.common.enums")
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
