package com.example.windy.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//当指定配置生效时才加载这个类
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple.retry", name = "enabled", havingValue = "true")
public class RepublishMessageRecoverConfig {

    /**
     * 声明消息确认异常交换机
     * @return 工作者模式交换机 交换机根据指定的Routing key发送到队列中
     */
    @Bean
    public DirectExchange republishExchange(){
        return new DirectExchange("error.exchange");
    }

    /**
     * 声明消息确认异常队列
     * @return 返回一个队列
     */
    @Bean
    public Queue republishQueue(){
        return new Queue("error.queue");
    }

    /**
     *
     * @param republishQueue republishQueue() 声明Queue Bean的方法名
     * @param republishExchange republishExchange() 声明DirectExchange Bean的方法名
     * @return
     */
    @Bean
    public Binding republishBinding(Queue republishQueue, DirectExchange republishExchange){
        //bind(Queue queue) to(DirectExchange exchange) with(String routingKey)

        return BindingBuilder.bind(republishQueue).to(republishExchange).with("error");
    }

    /**
     * 声明消费者消费消息失败的策略
     * @return  MessageRecoverer:重试耗尽后，将失败消息投递到指定的交换机
     */
    @Bean
    public MessageRecoverer republishMessageRecover(RabbitTemplate rabbitTemplate) {
        //RepublishMessageRecoverer(rabbitTemplate,"交换机","key")
        return new RepublishMessageRecoverer(rabbitTemplate,"error.exchange","error");
    }
}
