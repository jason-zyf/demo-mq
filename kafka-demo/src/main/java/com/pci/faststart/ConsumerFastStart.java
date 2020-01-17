package com.pci.faststart;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerFastStart {

    private static final String brokerList = "192.168.236.135:9092";
    // 主题名称-之前已经创建
    private static final String topic = "pci";
    // 消费组
    private static final String groupId = "group.demo";

    public static void main(String[] args) {

        Properties props = new Properties();

        // 必须设置的属性
        props.put("bootstrap.servers", brokerList);
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("group.id", "user");     // group.id 必须是已创建的消费者组，否则接受不到消息

        // 可选设置属性
        props.put("enable.auto.commit", "true");
        // 自动提交offset,每1s提交一次
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset","earliest ");
        props.put("client.id", "zy_client_id");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> records =
                    consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("topic = %s ,partition = %d,offset = %d, key = %s, value = %s%n",
                        record.topic(), record.partition(), record.offset(), record.key(), record.value());
            }
        }
    }

}
