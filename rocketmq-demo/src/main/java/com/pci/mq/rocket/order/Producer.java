package com.pci.mq.rocket.order;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

public class Producer {

    public static void main(String[] args) throws Exception {

        DefaultMQProducer producer = new DefaultMQProducer("group1");
        producer.setNamesrvAddr("192.168.236.130:9876;192.168.236.131:9876");
        producer.start();
        List<OrderStep> orderSteps = OrderStep.buildOrdes();

        for (int i = 0; i < orderSteps.size(); i++) {
            String body = orderSteps.get(i)+"";
            Message msg = new Message("orderTopic", "order", "i"+i, body.getBytes());
             SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                /**
                 * @param mqs 消息队列集合
                 * @param msg 对象
                 * @param arg 业务标识参数
                 * @return
                 */
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    long orderId = Long.valueOf(String.valueOf(arg)).longValue();
                    long index = orderId % mqs.size();
                    return mqs.get((int) index);
                }
            }, orderSteps.get(i).getOrderId());
            System.out.println("发送结果："+sendResult);
        }
        producer.shutdown();
    }

}
