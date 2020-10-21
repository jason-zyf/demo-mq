package com.pci;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@SpringBootApplication
@RestController
public class KafkaApplication {

    private static final Logger log = LoggerFactory.getLogger(KafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class,args);
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @Autowired
    private KafkaTemplate kafkaTemplate;
    private static final String topic = "pci";

    /**
     * 消息生产者
     */
    @GetMapping("/send/{input}")
    public String sendToKafka(@PathVariable String input) throws ExecutionException, InterruptedException {
        ListenableFuture send = this.kafkaTemplate.send(topic, input);
        Object o = send.get();
        System.out.println("send:"+send);
        System.out.println("sendGet:"+o);

        return "send success!,"+input;
    }

    /**
     * 消息接收
     */
    @KafkaListener(id = "fd",topics = topic,groupId = "group1")
    public void listenter(String input){
        System.out.println(input);
        log.info("接受消息：{}",input);
    }



    /**
     * 事务方式发送一
     */
    /*@GetMapping("/transationSend/{input}")
    public String transSendToKafa(@PathVariable String input){
        this.kafkaTemplate.send(topic,input);

        // 事务操作
        kafkaTemplate.executeInTransaction(t -> {
            t.send(topic, input);
            if ("error".equals(input)) {
                throw new RuntimeException("input is error");
            }
            t.send(topic, input + " anthor");
            return true;
        });
        return "send success!,"+input;
    }*/


    /**
     * 事务方式发送二
     */
    /*@GetMapping("/transSend/{input}")
    @Transactional(rollbackFor = RuntimeException.class)
    public String transSend(@PathVariable String input){

        kafkaTemplate.send(topic,input);

        if ("error".equals(input)) {
            throw new RuntimeException("input is error");
        }
        kafkaTemplate.send(topic, input + " anthor");

        return "send success!,"+input;
    }*/
    @GetMapping("/sendMax")
    public String sendMax() throws ExecutionException, InterruptedException {
        String testfile = FileUtil.fileToBase64("C:\\Users\\jason\\Desktop\\testfile");
        ListenableFuture send = this.kafkaTemplate.send(topic, testfile);
        Object o = send.get();
        System.out.println("send:"+send);

        return "sendMax success!";
    }

}
