package com.pci.mq.rocket.base.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.concurrent.TimeUnit;

/**
 * 发送异步消息
 */
public class AsyncProducer {

    public static void main(String[] args) throws Exception {
        // 1、创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2、设置nameserver地址
        producer.setNamesrvAddr("10.38.2.12:31076");
//        producer.setVipChannelEnabled(false);
        // 3、启动producer
        producer.start();

        for (int i = 0; i < 5; i++) {
            //4、创建消息对象，指定主题topic、tag和消息体
            Message msg = new Message("netCommandSend",
                    "VMlLacQTRg7n",
                    ("hello rocket_"+i).getBytes());

            //5、发送异步消息,返回结果
            producer.send(msg, new SendCallback() {
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送成功："+sendResult);
                }
                public void onException(Throwable e) {
                    System.out.println("发送异常："+e);
                }
            });

            //线程睡眠1秒
            TimeUnit.SECONDS.sleep(1);
        }
        // 6、关闭producer
        producer.shutdown();
    }

}
