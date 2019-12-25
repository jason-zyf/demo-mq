package com.pci.mq.rocket.base.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * 发送同步消息
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {
        // 1、创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2、设置nameserver地址
        producer.setNamesrvAddr("172.23.124.218:9876;172.23.127.76:9876");
        // 3、启动producer
        producer.start();

        for (int i = 0; i < 10; i++) {
            //4、创建消息对象，指定主题topic、tag和消息体
            Message msg = new Message("base",
                    "Tag1",
                    ("hello rocket_"+i).getBytes());

            //5、发送同步消息,返回结果
            SendResult result = producer.send(msg);
            // 消息发送状态
            SendStatus status = result.getSendStatus();
            // 消息ID
            String msgId = result.getMsgId();
            // 消息接收队列ID
            int queueId = result.getMessageQueue().getQueueId();

            System.out.println("发送结果："+result);

            //线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6、关闭producer
        producer.shutdown();
    }

}
