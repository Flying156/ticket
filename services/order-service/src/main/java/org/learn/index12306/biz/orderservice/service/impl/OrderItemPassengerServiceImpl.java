package org.learn.index12306.biz.orderservice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemPassengerDO;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemPassengerMapper;
import org.learn.index12306.biz.orderservice.service.OrderItemPassengerService;
import org.springframework.stereotype.Service;

/**
 * 订单、乘客服务接口实现
 *
 * @author Milk
 * @version 2023/10/21 19:47
 */
@Service
public class OrderItemPassengerServiceImpl extends ServiceImpl<OrderItemPassengerMapper, OrderItemPassengerDO> implements OrderItemPassengerService {
}
