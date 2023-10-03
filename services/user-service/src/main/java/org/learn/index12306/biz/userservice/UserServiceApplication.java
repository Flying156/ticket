package org.learn.index12306.biz.userservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 用户服务应用启动器
 *
 * @author Milk
 * @version 2023/10/1 13:50
 */
@SpringBootApplication
@MapperScan("org.learn.index12306.biz.userservice.dao.mapper")
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
