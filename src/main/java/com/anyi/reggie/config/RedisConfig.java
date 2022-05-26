package com.anyi.reggie.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author 安逸i
 * @version 1.0
 */
@Configuration
public class RedisConfig {
    @Bean("myRedisTemplate")
    public RedisTemplate<String ,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        // 定义一个自己的 String object 类型
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 配置Json序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // String 序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // 设置 String key 以 stringRedisSerializer 解析
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // 设置 Hash key 以 stringRedisSerializer 解析
        redisTemplate.setHashKeySerializer(stringRedisSerializer);

        // String value 以Json 解析
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // Hash value  以 Json 解析
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        return redisTemplate;
    }
}

