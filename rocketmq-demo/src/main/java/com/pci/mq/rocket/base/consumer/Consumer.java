package com.pci.mq.rocket.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消息接受者
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        // 1、创建消费者，并指定消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        // 2、消费者指定nameserver
        consumer.setNamesrvAddr("172.23.124.218:9876;172.23.127.76:9876");
        /**
         * 3、订阅主题和tag
         * tag类型含义
         * Tag1  只消费Tag1
         * Tag1||Tag2  消费Tag1和Tag2
         * *   消费此topic下的所有 Tag
         */
        consumer.subscribe("base", "Tag1");
        // 4、设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("接受到消息："+new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 5、启动消费者
        consumer.start();
        System.out.println("消费者启动成功");
    }

}
