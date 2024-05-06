package com.example.windy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 用来确认生产者将消息发送给交换器
 * 这里配置全局的ConfirmCallback,有的业务需要不同接口实现不同的回调后续，根据具体情况选择
 */
@Slf4j
@Component
public class RabbitConfirmCallbackConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取RabbitTemplate对象
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    // 消息成功到达Broker
                    log.info("消息成功到达Broker");
                } else {
                    // 消息未到达Broker，包含原因cause,进行失败的处理
                    log.info("消息未到达Broker: " + cause);
                }
            }

        });

    }
}

