package org.learn.index12306.biz.orderservice.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.learn.index12306.biz.orderservice.common.enums.OrderItemStatusEnum;
import org.learn.index12306.biz.orderservice.common.enums.OrderStatusEnum;
import org.learn.index12306.biz.orderservice.dao.entity.OrderDO;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemDO;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemPassengerDO;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemMapper;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemPassengerMapper;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderMapper;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderSelfPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailSelfRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;
import org.learn.index12306.biz.orderservice.remote.UserRemoteService;
import org.learn.index12306.biz.orderservice.remote.dto.UserQueryActualRespDTO;
import org.learn.index12306.biz.orderservice.service.OrderService;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.convention.page.PageResponse;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.database.toolkit.PageUtil;
import org.learn.index12306.framework.starter.user.core.UserContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单服务实现
 *
 * @author Milk
 * @version 2023/10/21 19:50
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends ServiceImpl<OrderMapper, OrderDO> implements OrderService {

    private final OrderItemMapper orderItemMapper;
    private final OrderItemPassengerMapper orderItemPassengerMapper;
    private final UserRemoteService userRemoteService;

    @Override
    public TicketOrderDetailRespDTO queryTicketOrderByOrderSn(String orderSn) {
        LambdaQueryWrapper<OrderDO>queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getOrderSn, orderSn);
        OrderDO orderDO = baseMapper.selectOne(queryWrapper);
        TicketOrderDetailRespDTO result = BeanUtil.convert(orderDO, TicketOrderDetailRespDTO.class);
        LambdaQueryWrapper<OrderItemDO>orderItemQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                .eq(OrderItemDO::getOrderSn, orderSn);
        List<OrderItemDO> orderItemDOList = orderItemMapper.selectList(orderItemQueryWrapper);
        result.setPassengerDetails(BeanUtil.convert(orderItemDOList, TicketOrderPassengerDetailRespDTO.class));

        return result;
    }

    @Override
    public PageResponse<TicketOrderDetailRespDTO> pageTicketOrder(TicketOrderPageQueryReqDTO requestParam) {
        LambdaQueryWrapper<OrderDO>queryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                .eq(OrderDO::getUserId, requestParam.getUserId())
                .in(OrderDO::getStatus, buildOrderStatusList(requestParam))
                .orderByDesc(OrderDO::getCreateTime);
        IPage<OrderDO> orderPage = baseMapper.selectPage(PageUtil.convert(requestParam), queryWrapper);
        return PageUtil.convert(orderPage, each ->{
            TicketOrderDetailRespDTO result = BeanUtil.convert(requestParam, TicketOrderDetailRespDTO.class);
            LambdaQueryWrapper<OrderItemDO> orderItemQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                    .eq(OrderItemDO::getOrderSn, each.getOrderSn());
            List<OrderItemDO>orderItemDOList = orderItemMapper.selectList(orderItemQueryWrapper);
            result.setPassengerDetails(BeanUtil.convert(orderItemDOList, TicketOrderPassengerDetailRespDTO.class));
            return result;
        });
    }

    @Override
    public PageResponse<TicketOrderDetailSelfRespDTO> pageSelfTicketOrder(TicketOrderSelfPageQueryReqDTO requestParam) {
        Result<UserQueryActualRespDTO> userActualResp  = userRemoteService.queryActualUserByUsername(UserContext.getUsername());
        LambdaQueryWrapper<OrderItemPassengerDO> queryWrapper = Wrappers.lambdaQuery(OrderItemPassengerDO.class)
                .eq(OrderItemPassengerDO::getIdCard, userActualResp.getData().getIdCard())
                .orderByDesc(OrderItemPassengerDO::getCreateTime);
        IPage<OrderItemPassengerDO> orderItemPassengerDOIpage = orderItemPassengerMapper.selectPage(PageUtil.convert(requestParam), queryWrapper);
        return PageUtil.convert(orderItemPassengerDOIpage, each ->{
            LambdaQueryWrapper<OrderDO> orderQueryWrapper = Wrappers.lambdaQuery(OrderDO.class)
                    .eq(OrderDO::getOrderSn, each.getOrderSn());
            OrderDO orderDO = baseMapper.selectOne(orderQueryWrapper);
            LambdaQueryWrapper<OrderItemDO> orderItemQueryWrapper = Wrappers.lambdaQuery(OrderItemDO.class)
                    .eq(OrderItemDO::getOrderSn, each.getOrderSn())
                    .eq(OrderItemDO::getIdCard, each.getIdCard());
            OrderItemDO orderItemDO = orderItemMapper.selectOne(orderItemQueryWrapper);
            TicketOrderDetailSelfRespDTO actualResult = BeanUtil.convert(orderDO, TicketOrderDetailSelfRespDTO.class);
            BeanUtil.convertIgnoreNullAndBlank(orderItemDO, actualResult);
            return actualResult;
        });
    }

    private List<Integer> buildOrderStatusList(TicketOrderPageQueryReqDTO requestParam){
        List<Integer>result = new ArrayList<>();
        switch (requestParam.getStatusType()){
            case 0 -> result = ListUtil.of(
                    OrderStatusEnum.PENDING_PAYMENT.getStatus()
            );
            case 1 -> result = ListUtil.of(
                    OrderStatusEnum.ALREADY_PAID.getStatus(),
                    OrderStatusEnum.PARTIAL_REFUND.getStatus(),
                    OrderStatusEnum.FULL_REFUND.getStatus()
            );
            case 2 -> result = ListUtil.of(
                    OrderStatusEnum.COMPLETED.getStatus()
            );
        }
        return result;
    }
}
