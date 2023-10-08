package org.learn.index12306.biz.ticketservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 购票服务应用启动器
 *
 * @author Milk
 * @version 2023/10/4 10:48
 */
@SpringBootApplication
@MapperScan("org.learn.index12306.biz.ticketservice.dao.mapper")
public class TicketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketServiceApplication.class, args);
    }
}
