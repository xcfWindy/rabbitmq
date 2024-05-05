package com.example.windy.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author cf
 * @Date 2022/7/23 12:01
 * @Description:
 */
@Component
@Slf4j
public class SpringRabbitListener {

    //    @RabbitListener(queues = "simple.queue")
//    public void listenSimpleQueueMessage(String msg) throws InterruptedException {
//        System.out.println("spring 消费者接收到消息：【" + msg + "】");
//    }
    int count1 = 0;
    int count2 = 0;

    @RabbitListener(queues = "simple.queue1")
    public void listenWorkQueue1(String msg) throws Exception {
        Thread.sleep(20);
        count1++;
        log.info("消费者1接收到消息{},接收第{}条", msg, count1);
    }

    @RabbitListener(queues = "simple.queue1")
    public void listenWorkQueue2(String msg) throws Exception {
        Thread.sleep(100);
        count2++;
        log.info("消费者2接收到消息{},接收第{}条", msg, count2);

    }

    @RabbitListener(queues = "fanout.queue1")
    public void fanoutQueue1(String msg) throws Exception {

        log.info("消费者1接收到消息{}", msg);

    }

    @RabbitListener(queues = "fanout.queue2")
    public void fanoutQueue2(String msg) throws Exception {
        log.info("消费者2接收到消息{}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue1"),
            exchange = @Exchange(value = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "blue"}))
    public void listenDirectQueue1(String msg) {
        log.info("消费者接收到direct.queue1的消息{}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "direct.queue2"),
            exchange = @Exchange(value = "itcast.direct", type = ExchangeTypes.DIRECT),
            key = {"red", "yellow"}))
    public void listenDirectQueue2(String msg) {
        log.info("消费者接收到direct.queue2的消息{}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(name = "topic.queue1"),
            exchange = @Exchange(value = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "china.#"))
    public void listenTopicQueue1(String msg) {
        log.info("消费者接收到topic.queue1的消息{}", msg);
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue(value = "topic.queue2"),
            exchange = @Exchange(value = "itcast.topic", type = ExchangeTypes.TOPIC),
            key = "#.news"))
    public void listenTopicQueue2(String msg) {
        log.info("消费者接收到topic.queue2的消息{}", msg);
    }

    @RabbitListener(queues = "object.queue")
    public void fan(Map msg) throws Exception {
        log.info("消费者1接收到消息{}", msg);

    }
}
