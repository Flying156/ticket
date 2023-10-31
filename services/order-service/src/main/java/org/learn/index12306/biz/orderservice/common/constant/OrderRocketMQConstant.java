package org.learn.index12306.biz.orderservice.common.constant;

/**
 * RocketMQ 订单服务常量类
 *
 * @author Milk
 * @version 2023/10/23 20:34
 */
public final class OrderRocketMQConstant {

    /**
     * 订单服务相关业务 Topic Key
     */
    public static final String ORDER_DELAY_CLOSE_TOPIC_KEY = "index12306_order-service_delay-close-order_topic${unique-name:}";

    /**
     * 购票服务创建订单后延时关闭业务 Tag Key
     */
    public static final String ORDER_DELAY_CLOSE_TAG_KEY = "index12306_order-service_delay-close-order_tag${unique-name:}";
}
