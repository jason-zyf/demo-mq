package com.migrate.redis.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

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

    @GetMapping("/insertPointData")
    public String insertPointData(){

        File file = new File("C:\\Users\\jason\\Desktop\\三维用到的点位.txt");//文件路径
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);

            LineNumberReader reader = new LineNumberReader(fileReader);
            String txt = "";
            while (txt != null) {
                txt = reader.readLine();
                int value = RandomUtils.nextInt(0,2);
                if(txt.endsWith("Temperatur")){
                    value = RandomUtils.nextInt(20,40);
                }else if(txt.endsWith("Humidity")){
                    value = RandomUtils.nextInt(10,40);
                }
                Map<String,Object> map = new HashMap<>();
                map.put("Point", txt+".Value");
                map.put("Quality", 192);
                map.put("Timestamp", System.currentTimeMillis()+"");
                map.put("Value", value+"");

                Object o = JSONObject.toJSON(map);
                redisJsonTemplate.opsForValue().set(txt+".Value", o);
            }
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        return "插入成功";
    }

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        File file = new File("C:\\Users\\jason\\Desktop\\三维用到的点位.txt");//文件路径
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);

            LineNumberReader reader = new LineNumberReader(fileReader);
            String txt = "";
            while (txt != null) {
                txt = reader.readLine();
                System.out.println( txt + "\n");
                long timeEnd = System.currentTimeMillis();
                System.out.println("总共花费：" + (timeEnd - timeStart) + "ms");
            }
            reader.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


}
