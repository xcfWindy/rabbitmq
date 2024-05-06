package com.example.windy;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class ConfirmAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void testPublisherConfirm() throws InterruptedException {
        // 交换机名称
        String exchangeName = "1windy.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                if (ack) {
                    // 消息成功到达Broker
                    System.out.println("消息成功到达Broker");
                } else {
                    // 消息未到达Broker，包含原因cause
                    System.out.println("消息未到达Broker: " + cause);
                    String id = correlationData.getId();
                    //可以将消息的id做为key存储在redis中，来进行消息重发
                    rabbitTemplate.convertAndSend(exchangeName, "11.ne1ws", message, new CorrelationData());
                }
            }
        });

        rabbitTemplate.convertAndSend(exchangeName, "11.ne1ws", message, new CorrelationData());

        TimeUnit.SECONDS.sleep(20);
    }

    @Test
    void testPublisherConfirm2() throws InterruptedException {

        // 交换机名称
        String exchangeName = "1windy.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        rabbitTemplate.convertAndSend(exchangeName, "11.ne1ws", message, new CorrelationData());

        TimeUnit.SECONDS.sleep(2);
    }

    @Test
    void testPublisherConfirm3() throws InterruptedException {

        // 交换机名称
        String exchangeName = "windy.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        rabbitTemplate.convertAndSend(exchangeName, "adsadas", message, new CorrelationData());

        TimeUnit.SECONDS.sleep(2);
    }

    /**
     * 插入一百万消息到消息队列中，查看生产者消息确认机制的影响
     *
     */
    @Test
    void testPageOut() {
        Message message = MessageBuilder.withBody("hello".getBytes(StandardCharsets.UTF_8))
                .setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT).build();

        for (int i = 0; i < 1000000; i++) {
            rabbitTemplate.convertAndSend("simple.queue", message);
        }

    }
}
