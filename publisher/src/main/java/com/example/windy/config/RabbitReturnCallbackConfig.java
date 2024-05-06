package com.example.windy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 当Spring容器初始化完成后会检查实现ApplicationContextAware接口的类，调用setApplicationContext方法
 *  ReturnCallback用来确认交换器传递给队列的过程中消息是否成功投递,交换机到队列的MQ内部发送实现，所以配置全局的就行
 *  生产环境不要开启publisher-returns: true此配置
 */
@Slf4j
@Configuration
public class RabbitReturnCallbackConfig implements ApplicationContextAware {

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //1.获取RabbitTemplate
        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        //2.设置ReturnCallback
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.info("消息路由失败,应答码{},原因{},交换机{},路由键{},消息{}",
                        returnedMessage.getReplyCode(),
                        returnedMessage.getReplyText(),
                        returnedMessage.getExchange(),
                        returnedMessage.getRoutingKey(),
                        returnedMessage.getMessage().toString());
            }
        });
    }
}
