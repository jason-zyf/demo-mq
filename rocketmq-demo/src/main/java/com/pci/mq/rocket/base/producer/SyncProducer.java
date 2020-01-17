package com.pci.mq.rocket.base.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 发送同步消息
 */
public class SyncProducer {

    public static void main(String[] args) throws Exception {
        // 1、创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2、设置nameserver地址
        producer.setNamesrvAddr("192.168.236.135:9876;192.168.236.134:9876");
        // 3、启动producer
        producer.start();

        // 获取某个主题下的所有队列集合
//        List<MessageQueue> queueList = producer.fetchPublishMessageQueues("testrocketmqdoc");
//        System.out.println(queueList.toString());

        /*MessageExt ext = producer.viewMessage("C0A8EC8200002A9F0000000000B33E90");
        MessageExt ext1 = producer.viewMessage("testMsgID", "AC17788B4B0418B4AAC245FFB5110000");

        System.out.println("通过offsetMsgId查询："+ext.toString());
        System.out.println("通过topic和msgId查询："+ext1.toString());*/

        for (int i = 0; i < 10; i++) {
            //4、创建消息对象，指定主题topic、tag和消息体
            Message msg = new Message("testMsgID",
                    "Tag",
                    ("hello rocket_"+i).getBytes());

//            Message msg1 = new Message("NoTag", "hah".getBytes());
            // 构造一个消息队列
            MessageQueue queue = new MessageQueue("NoTag", "broker-b", 3);

            //5、发送同步消息,返回结果
            SendResult result = producer.send(msg);
            // 消息发送状态
            SendStatus status = result.getSendStatus();
            // 消息ID
            String msgId = result.getMsgId();
            // 消息接收队列ID
            int queueId = result.getMessageQueue().getQueueId();

            System.out.println("发送结果："+result);
//            System.out.println("status:"+status+",messageQueue:"+result.getMessageQueue());

            //线程睡眠1秒
            TimeUnit.SECONDS.sleep(10);
        }
        // 6、关闭producer
        producer.shutdown();
    }

}
