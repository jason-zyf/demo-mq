package com.pci.springboot.rocketmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProducerController {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @GetMapping("/sendMessage")
    public String sendMessage(){
        rocketMQTemplate.convertAndSend("springboot-mq", "hello pci-rocketmq");
        log.info("消息发送成功");

        return "success";
    }


}
