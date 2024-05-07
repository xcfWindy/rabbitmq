package com.example.windy.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TTLListener {

    @RabbitListener(bindings =
    @QueueBinding(
            value = @Queue(name = "test.dlx.queue",
                    arguments = {
                            //普通队列绑定死信交换机
                            @Argument(name = "x-dead-letter-exchange", value = "dlx.topic"),
                            //设置发送给死信交换机的routingkey
                            @Argument(name = "x-dead-letter-routing-key", value = "dlx.windy"),
                            //设置队列的过期时间ttl（10秒）
                            @Argument(name = "x-message-ttl", value = "100000", type = "java.lang.Integer"),
                            //设置队列的长度限制max-length
                            @Argument(name = "x-max-length", value = "10", type = "java.lang.Integer"),}),
            exchange = @Exchange(name = "test.dlx.direct", type = ExchangeTypes.DIRECT),
            key = "test.dlx")
    )
    //正常消费者，绑定正常交换机和正常队列，并将队列与死信交换机绑定
    public void testDlxQueueMessage(String msg) throws Exception {
        //模拟阻塞
        TimeUnit.SECONDS.sleep(11);
        log.info("正常消费一条信息:" + msg);
    }
}
