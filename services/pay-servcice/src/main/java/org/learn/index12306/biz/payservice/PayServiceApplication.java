package org.learn.index12306.biz.payservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 支付启动类
 *
 * @author Milk
 * @version 2023/11/6 21:59
 */
@SpringBootApplication
@MapperScan("org.learn.index12306.biz.payservice.dao.mapper")
@EnableFeignClients("org.learn.index12306.biz.payservice.remote")
public class PayServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class, args);
    }
}
