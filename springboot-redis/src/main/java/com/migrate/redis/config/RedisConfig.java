package com.migrate.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author zyting
 * @sinne 2020-06-16
 */
@Configuration
public class RedisConfig {

    @Bean("redisTemplate")
    @Primary
    public RedisTemplate<String,Object> getRedisTemplate(){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(getJedisConnectionFactory());

        // redis存取对象的关键配置
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(serializer);
        // 为hashvalue添加序列化和反序列化类
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @Primary
    public RedisConnectionFactory getJedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory=new JedisConnectionFactory();
        jedisConnectionFactory.setHostName("10.33.20.21");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setDatabase(0);
        return jedisConnectionFactory;
    }

    @Bean("redisJsonTemplate")
    public RedisTemplate<String, Object> redisJsonTemplate(){
        RedisTemplate redisJsonTemplate = new RedisTemplate();
        redisJsonTemplate.setConnectionFactory(getJedisConnectionJsonFactory());

        // redis存取对象的关键配置
        redisJsonTemplate.setKeySerializer(new StringRedisSerializer());
        redisJsonTemplate.setHashKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisJsonTemplate.setValueSerializer(serializer);
        // 为hashvalue添加序列化和反序列化类
        redisJsonTemplate.setHashValueSerializer(serializer);
        redisJsonTemplate.afterPropertiesSet();
        return redisJsonTemplate;
    }

    @Bean
    public RedisConnectionFactory getJedisConnectionJsonFactory() {
        JedisConnectionFactory jedisConnectionJsonFactory= new JedisConnectionFactory();
        jedisConnectionJsonFactory.setHostName("10.38.2.12");
        jedisConnectionJsonFactory.setPort(30012);
        jedisConnectionJsonFactory.setDatabase(1);
        return jedisConnectionJsonFactory;
    }


}
