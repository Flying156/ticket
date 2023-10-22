package org.learn.index12306.biz.orderservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemDO;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemMapper;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;
import org.learn.index12306.biz.orderservice.service.OrderItemService;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 订单详情服务实现
 *
 * @author Milk
 * @version 2023/10/21 19:49
 */
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItemDO> implements OrderItemService {


    @Override
    public List<TicketOrderPassengerDetailRespDTO> queryTicketItemOrderById(TicketOrderItemQueryReqDTO requestParam) {
        LambdaQueryWrapper<OrderItemDO> queryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                .eq(OrderItemDO::getOrderSn, requestParam.getOrderSn())
                .in(OrderItemDO::getId, requestParam.getOrderItemRecordIds());
        List<OrderItemDO>orderItemDOList = baseMapper.selectList(queryWrapper);
        return BeanUtil.convert(orderItemDOList, TicketOrderPassengerDetailRespDTO.class);
    }
}
