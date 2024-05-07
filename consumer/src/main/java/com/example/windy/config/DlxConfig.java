package com.example.windy.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Configuration
public class DlxConfig {
//    /**
//     * 声明测试死信队列
//     * @return 测试死信队列
//     */
//    @Bean
    public Queue testDlxQueue(){

//        return QueueBuilder.durable("test.dlx.queue") // 指定队列名称，并持久化
//                .ttl(10000) // 设置队列的超时时间，10秒
//                .deadLetterExchange("dlx.topic") // 指定死信交换机
//                .deadLetterRoutingKey("dlx.windy") // 指定死信交换机key
//                .build();

        Map<String, Object> arguments = new HashMap<>();
        //message在该队列queue的存活时间最大为10秒
        arguments.put("x-message-ttl", 10000);
        //x-dead-letter-exchange参数是设置该队列的死信交换器（DLX）
        arguments.put("x-dead-letter-exchange", "dlx.topic");
        //x-dead-letter-routing-key参数是给这个DLX指定路由键
        arguments.put("x-dead-letter-routing-key", "dlx.windy");
        return new Queue("test.dlx.queue", true, true, false, arguments);
    }



    /**
     * 声明死信队列
     * @return 死信队列
     */
    @Bean
    public Queue dlxQueue(){
        return new Queue("dlx.queue");
    }

    /**
     * 声明死信交换机
     * @return 死信交换机
     */
    @Bean
    public TopicExchange dlxTopic(){
        return new TopicExchange("dlx.topic");
    }

    /**
     * 绑定死信队列和交换机
     */
    @Bean
    public Binding dlxBinding(TopicExchange dlxTopic,Queue dlxQueue){
        return BindingBuilder.bind(dlxQueue).to(dlxTopic).with("dlx.#");
    }
}
