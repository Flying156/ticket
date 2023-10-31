package org.learn.index12306.biz.orderservice.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.learn.index12306.biz.orderservice.common.enums.OrderItemStatusEnum;
import org.learn.index12306.biz.orderservice.common.enums.OrderStatusEnum;
import org.learn.index12306.biz.orderservice.dao.entity.OrderDO;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemDO;
import org.learn.index12306.biz.orderservice.dao.entity.OrderItemPassengerDO;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemMapper;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderItemPassengerMapper;
import org.learn.index12306.biz.orderservice.dao.mapper.OrderMapper;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderItemCreateReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.req.TicketOrderSelfPageQueryReqDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderDetailSelfRespDTO;
import org.learn.index12306.biz.orderservice.dto.resp.TicketOrderPassengerDetailRespDTO;
import org.learn.index12306.biz.orderservice.mq.event.DelayCloseOrderEvent;
import org.learn.index12306.biz.orderservice.mq.produce.DelayCloseOrderSendProduce;
import org.learn.index12306.biz.orderservice.remote.UserRemoteService;
import org.learn.index12306.biz.orderservice.remote.dto.UserQueryActualRespDTO;
import org.learn.index12306.biz.orderservice.service.OrderItemPassengerService;
import org.learn.index12306.biz.orderservice.service.OrderItemService;
import org.learn.index12306.biz.orderservice.service.OrderService;
import org.learn.index12306.biz.orderservice.service.orderid.OrderIdGeneratorManager;
import org.learn.index12306.framework.starter.common.toolkit.BeanUtil;
import org.learn.index12306.framework.starter.convention.exception.ServiceException;
import org.learn.index12306.framework.starter.convention.page.PageResponse;
import org.learn.index12306.framework.starter.convention.result.Result;
import org.learn.index12306.framework.starter.database.toolkit.PageUtil;
import org.learn.index12306.framework.starter.user.core.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private final OrderItemService orderItemService;
    private final OrderItemPassengerService orderItemPassengerService;
    private final DelayCloseOrderSendProduce delayCloseOrderSendProduce;

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

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public String createTicketOrder(TicketOrderCreateReqDTO requestParam) {
        // 创建全局唯一的订单号
        String orderSn = OrderIdGeneratorManager.generateId(requestParam.getUserId());
        OrderDO orderDO = OrderDO.builder()
                .orderSn(orderSn)
                .orderTime(requestParam.getOrderTime())
                .departure(requestParam.getDeparture())
                .departureTime(requestParam.getDepartureTime())
                .ridingDate(requestParam.getRidingDate())
                .arrivalTime(requestParam.getArrivalTime())
                .trainNumber(requestParam.getTrainNumber())
                .arrival(requestParam.getArrival())
                .trainId(requestParam.getTrainId())
                .source(requestParam.getSource())
                .status(OrderStatusEnum.PENDING_PAYMENT.getStatus())
                .username(requestParam.getUsername())
                .userId(String.valueOf(requestParam.getUserId()))
                .build();
        baseMapper.insert(orderDO);

        List<TicketOrderItemCreateReqDTO> ticketOrderItem = requestParam.getTicketOrderItems();
        List<OrderItemDO> orderItemDOList = new ArrayList<>();
        List<OrderItemPassengerDO> orderItemPassengerDOList = new ArrayList<>();
        ticketOrderItem.forEach(each ->{
            OrderItemDO orderItemDO = OrderItemDO.builder()
                    .trainId(requestParam.getTrainId())
                    .seatNumber(each.getSeatNumber())
                    .carriageNumber(each.getCarriageNumber())
                    .realName(each.getRealName())
                    .orderSn(orderSn)
                    .phone(each.getPhone())
                    .seatType(each.getSeatType())
                    .username(requestParam.getUsername()).amount(each.getAmount()).carriageNumber(each.getCarriageNumber())
                    .idCard(each.getIdCard())
                    .ticketType(each.getTicketType())
                    .idType(each.getIdType())
                    .userId(String.valueOf(requestParam.getUserId()))
                    .status(0)
                    .build();
            orderItemDOList.add(orderItemDO);

            OrderItemPassengerDO orderItemPassengerDO = OrderItemPassengerDO.builder()
                    .idType(each.getIdType())
                    .idCard(each.getIdCard())
                    .orderSn(orderSn)
                    .build();
            orderItemPassengerDOList.add(orderItemPassengerDO);
        });
        orderItemService.saveBatch(orderItemDOList);
        orderItemPassengerService.saveBatch(orderItemPassengerDOList);
        try{
            // 发送 RocketMQ 延迟消息，指定时间后取消订单
            DelayCloseOrderEvent event = DelayCloseOrderEvent.builder()
                    .trainId(String.valueOf(requestParam.getTrainId()))
                    .departure(requestParam.getDeparture())
                    .arrival(requestParam.getArrival())
                    .orderSn(orderSn)
                    .trainPurchaseTicketResults(requestParam.getTicketOrderItems())
                    .build();
            // 创建订单并支付后闫氏关闭订单消息
            SendResult sendResult = delayCloseOrderSendProduce.sendMessage(event);
            if(!Objects.equals(sendResult.getSendStatus(), SendStatus.SEND_OK)){
                throw new ServiceException("投递延迟关闭订单消息队列失败");
            }
        }catch(Throwable ex){
            log.error("延迟关闭订单消息队列发送错误，请求参数：{}", JSON.toJSONString(requestParam), ex);
            throw ex;
        }

        return orderSn;
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
