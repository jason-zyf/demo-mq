package com.pci.mq.rocket.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

public class Consumer {

    public static void main(String[] args) throws Exception {

        // 1、创建消费者，并指定消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        // 2、消费者指定nameserver
        consumer.setNamesrvAddr("192.168.236.130:9876;192.168.236.131:9876");
        /**
         * 3、订阅主题和tag
         * tag类型含义
         * Tag1  只消费Tag1
         * Tag1||Tag2  消费Tag1和Tag2
         * *   消费此topic下的所有 Tag
         */
        consumer.subscribe("orderTopic", "order");
        // 4、设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerOrderly() {
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("线程名称：【"+Thread.currentThread().getName()+"】，"+new String(msg.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        // 5、启动消费者
        consumer.start();

        System.out.println("消费者已启动");
    }

}
