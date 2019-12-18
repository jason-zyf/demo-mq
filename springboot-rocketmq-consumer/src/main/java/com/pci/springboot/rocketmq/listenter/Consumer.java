package com.pci.springboot.rocketmq.listenter;


import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RocketMQMessageListener(topic = "springboot-mq",consumerGroup = "${rocketmq.consumer.group}")
public class Consumer implements RocketMQListener<String> {

    @Override
    public void onMessage(String s) {
        log.info("接受到消息："+ s);
    }

}
