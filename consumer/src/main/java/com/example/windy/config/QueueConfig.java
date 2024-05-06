package com.example.windy.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 基于@Bean的方式声明队列和交换机比较麻烦，Spring还提供了基于注解方式来声明。
 * 在consumer的SpringRabbitListener中添加两个消费者，同时基于注解来声明队列和交换机：
 */
@Configuration
public class QueueConfig {
    /**
     * 声明队列
     * @return simple.queue队列
     */
    @Bean
    public Queue queue(){
        return new Queue("simple.queue");
    }
    /**
     * 声明队列
     * @return simple.queue1队列
     */
    @Bean
    public Queue queue1(){
        return new Queue("simple.queue1");
    }
    /**
     * 声明交换机
     * @return Fanout类型交换机 广播
     */
    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("itcast.fanout");
    }
    /**
     * 声明队列
     * @return simple.queue1队列
     */
    @Bean
    public Queue fanoutQueue1(){
        return new Queue("fanout.queue1");
    }
    /**
     * 声明队列
     * @return simple.queue1队列
     */
    @Bean
    public Queue fanoutQueue2(){
        return new Queue("fanout.queue2");
    }

    /**
     * 绑定
     * @param fanoutExchange
     * @param fanoutQueue1
     * @return
     */
    @Bean
    public Binding binding1(FanoutExchange fanoutExchange,Queue fanoutQueue1){
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }
    @Bean
    public Binding binding2(FanoutExchange fanoutExchange,Queue fanoutQueue2){
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }

    @Bean
    public Queue mapQueue2(){
        return new Queue("object.queue");
    }
}
