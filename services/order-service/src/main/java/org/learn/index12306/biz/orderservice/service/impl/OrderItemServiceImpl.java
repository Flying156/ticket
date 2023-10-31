package org.learn.index12306.biz.orderservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.learn.index12306.biz.orderservice.common.enums.OrderStatusEnum;
import org.learn.index12306.biz.orderservice.dao.entity.OrderDO;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemDO;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemPassengerDO;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemMapper;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemPassengerMapper;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderMapper;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;
import org.learn.index12306.biz.orderservice.service.OrderItemPassengerService;
import org.learn.index12306.biz.orderservice.service.OrderItemService;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单详情服务实现
 *
 * @author Milk
 * @version 2023/10/21 19:49
 */
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemDO> implements OrderItemService {

    private final OrderMapper orderMapper;
    private final OrderItemPassengerService orderItemPassengerService;

    @Override
    public List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam) {
        LambdaQueryWrapper<OrderItemDO> queryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                .eq(OrderItemDO::getOrderSn, requestParam.getOrderSn())
                .in(OrderItemDO::getId, requestParam.getOrderItemRecordIds());
        List<OrderItemDO>orderItemDOList = baseMapper.selectList(queryWrapper);
        return BeanUtil.convert(orderItemDOList, TicketOrderPassengerDetailRespDTO.class);
    }


}
