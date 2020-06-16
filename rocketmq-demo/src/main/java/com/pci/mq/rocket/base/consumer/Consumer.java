package com.pci.mq.rocket.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消息接受者
 */
public class Consumer {

    public static void main(String[] args) throws Exception {
        // 1、创建消费者，并指定消费组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group9334");

        // 2、消费者指定nameserver
        consumer.setNamesrvAddr("10.38.2.12:30076");

//        ConsumeFromWhere consumeFromWhere = consumer.getConsumeFromWhere();
//        System.out.println(consumeFromWhere);     // CONSUME_FROM_LAST_OFFSET

        // ConsumeFromWhere.CONSUME_FROM_TIMESTAMP  设置从启动时开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // 可以设置从什么时间开始消费，配合setConsumeTimestamp一起使用默认半小时之前的,格式yyyyMMddhhmmss
//        consumer.setConsumeTimestamp(UtilAll.timeMillisToHumanString3(System.currentTimeMillis() - 1800000L));

//        consumer.setMessageModel(MessageModel.BROADCASTING);  // 设置为广播模式
        /**
         * 3、订阅主题和tag
         * tag类型含义
         * Tag1  只消费Tag1
         * Tag1||Tag2  消费Tag1和Tag2
         * *   消费此topic下的所有 Tag
         * 如果需要订阅多个主题，继续在后面多写几个 consumer.subscribe(topic, "*"); 即可
         */
//        consumer.subscribe("pci", "*");
        consumer.subscribe("netCommandSend", "N33");
//        consumer.subscribe("pci-hj", "*");

//        consumer.unsubscribe("pci");   // 取消订阅
        // 4、设置回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    String str = new String(msg.getBody());
                        System.out.println("c#:"+str);
//                    System.out.println("接受到消息："+new String(msg.getBody())+",bornHost:"+msg.getBornHost()+",msgId:"
//                     +msg.getMsgId()+",queueId:"+msg.getQueueId()+",queueOffset:"+msg.getQueueOffset()+",CommitLogOffset:"+msg.getCommitLogOffset());
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        // 5、启动消费者
        consumer.start();
        System.out.println("消费者启动成功");
    }

}
