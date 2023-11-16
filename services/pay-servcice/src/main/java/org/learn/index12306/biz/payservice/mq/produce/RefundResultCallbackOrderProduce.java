package org.learn.index12306.biz.payservice.mq.produce;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.learn.index12306.biz.payservice.mq.domain.MessageWrapper;
import org.learn.index12306.biz.payservice.mq.event.RefundResultCallbackOrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static org.learn.index12306.biz.payservice.common.constant.PayRocketMQConstant.PAY_GLOBAL_TOPIC_KEY;
import static org.learn.index12306.biz.payservice.common.constant.PayRocketMQConstant.REFUND_RESULT_CALLBACK_TAG_KEY;

/**
 * 退款回调事件生产者
 *
 * @author Milk
 * @version 2023/11/15 13:15
 */
@Component
public class RefundResultCallbackOrderProduce extends AbstractCommonSendProduceTemplate<RefundResultCallbackOrderEvent>{

    private final ConfigurableEnvironment environment;
    public RefundResultCallbackOrderProduce(@Autowired RocketMQTemplate rocketMQTemplate, @Autowired ConfigurableEnvironment environment) {
        super(rocketMQTemplate);
        this.environment = environment;
    }

    @Override
    protected BaseSendExtendDTO buildBaseSendExtendParam(RefundResultCallbackOrderEvent messageSendEvent) {
        return BaseSendExtendDTO.builder()
                .eventName("退款回调事件")
                .keys(messageSendEvent.getOrderSn())
                .topic(environment.resolvePlaceholders(PAY_GLOBAL_TOPIC_KEY))
                .tag(environment.resolvePlaceholders(REFUND_RESULT_CALLBACK_TAG_KEY))
                .sentTimeout(2000L)
                .build();
    }

    @Override
    protected Message<?> buildMessage(RefundResultCallbackOrderEvent messageSendEvent, BaseSendExtendDTO requestParam) {
        String key = StrUtil.isBlank(requestParam.getKeys()) ? UUID.randomUUID().toString() : requestParam.getKeys();
        return MessageBuilder
                .withPayload(new MessageWrapper(requestParam.getKeys(), messageSendEvent))
                .setHeader(MessageConst.PROPERTY_KEYS, key)
                .setHeader(MessageConst.PROPERTY_TAGS, requestParam.getTag())
                .build();
    }


}
