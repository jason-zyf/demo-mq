package com.pci.faststart;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerFastStart {

    // 10.36.10.2:9092;10.36.10.3:9092;10.36.10.4:9092
    private static final String brokerList = "10.36.10.2:9092;10.36.10.3:9092;10.36.10.4:9092";
    // 主题名称-之前已经创建
    private static final String topic = "lilt_test";
    // 消费组
    private static final String groupId = "userGroup";

    public static void main(String[] args) {

        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", brokerList);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "user1");     // group.id 必须是已创建的消费者组，否则接受不到消息

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset","earliest ");
        props.put("client.id", groupId);

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        // 获取所有的主题信息
        /*Map<String, List<PartitionInfo>> stringListMap = consumer.listTopics();
        for (Map.Entry<String, List<PartitionInfo>> entry : stringListMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }*/
        // 设置从topicPartition中的指定 offset 开始消费消息
        ConsumerRecords<String, String> recordTemp = consumer.poll(0);
//        System.out.println(recordTemp.isEmpty());
        consumer.seek(new TopicPartition("lilt_test", 0), 31);


        try {
            while (true) {
                ConsumerRecords<String, String> records =
                        consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n",
                            record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
                consumer.commitAsync();
            }
        }finally {
            consumer.commitSync();
            consumer.close();
        }
    }

}
