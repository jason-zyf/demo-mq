package com.migrate.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author zyting
 * @sinne 2020-06-16
 */
@RestController
public class RedisController {

    //  10.33.20.21
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String,Object> redisTemplate;

    // 10.38.2.12
    @Autowired
    @Qualifier("redisJsonTemplate")
    private RedisTemplate<String,Object> redisJsonTemplate;

    @GetMapping("/index")
    public String index(){
        return "测试一下";
    }

    @GetMapping("/findCache")
    public String findCache(){

        Object xsh = redisTemplate.opsForValue().get("HMI:CTS:PSCADA:CTS.PSCADA.110KV.1#DLBBTCK.aiiPWRC-OilTemp1.Quality");
        return xsh.toString();
    }

    @GetMapping("/findJsonCache")
    public String findJsonCache(){

        Object xsh = redisJsonTemplate.opsForValue().get("HMI:CTS:PSCADA:CTS.PSCADA.110KV.1#DLBBTCK.DevSelect");
        return xsh.toString();
    }

    @GetMapping("/insertCacheData")
    public String insertCacheData(String pattern){

        Set<String> keys = redisTemplate.keys(pattern+":*");
        List<String> keyList = new ArrayList<>(keys);
        System.out.println(keyList.size());

        for(int i= 0;i<keyList.size();i++){
            Object value = redisTemplate.opsForValue().get(keyList.get(i));
            redisJsonTemplate.opsForValue().set(keyList.get(i), value);
        }
        return "成功插入："+keys.size();
    }



}
