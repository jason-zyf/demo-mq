package com.pci.faststart;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.Future;

public class ProducerFastStart {
    // Kafka集群地址 10.36.10.2:9092;10.36.10.3:9092;10.36.10.4:9092
    private static final String brokerList = "172.23.125.15:9092";
    // 主题名称-之前已经创建
    private static final String topic = "logtest";

    public static void main(String[] args) {

        Properties properties = new Properties();
        //设置key序列化器
//        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 设置重置次数
        properties.put(ProducerConfig.RETRIES_CONFIG, 10);
        // 设置值序列化
//        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        //设置集群地址
//        properties.put("bootstrap.servers", brokerList);
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);

        // KafkaProducer 线程安全
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);

        try {
            for(int i= 0; i < 2; i++){
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, "测试一下_"+i);
//            producer.send(record);   // 单向
                RecordMetadata recordMetadata = producer.send(record).get();
                System.out.println("part:" + recordMetadata.partition() + ";topic:" + recordMetadata.topic());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        producer.close();
    }
}
