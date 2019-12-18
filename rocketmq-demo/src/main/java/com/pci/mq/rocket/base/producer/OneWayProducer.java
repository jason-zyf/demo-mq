package com.pci.mq.rocket.base.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * 发送单向消息
 */
public class OneWayProducer {

    public static void main(String[] args) throws Exception {
        // 1、创建生产者，并指定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        // 2、指定nameserver
        producer.setNamesrvAddr("172.23.126.41:9876;172.23.122.206:9876");
        // 3、启动生产者
        producer.start();

        for (int i = 0; i < 5; i++) {
            // 4、创建消息对象，并指定topic、tag和消息体
            Message msg = new Message("base",
                    "Tag3",
                    ("hello rocket_"+i).getBytes());
            // 5、发送单向消息，比如日志，如需接到响应
            producer.sendOneway(msg);
        }
        // 6、关闭生产者
        producer.shutdown();
    }

}
