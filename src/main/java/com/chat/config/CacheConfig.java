package com.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;

/**
 * @author loveyouso
 * @create 2018-09-18 13:57
 **/
public class CacheConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    @Value("$(spring.redis.timeout)")
    private int timeout;

    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate<String,String> redisTemplate){
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        //设置key-value过期时间
        redisCacheManager.setDefaultExpiration(timeout);
        LOGGER.info("初始化redis缓存管理器完成");
        return redisCacheManager;
    }

    public KeyGenerator wiselyKeyGenerator(){
        return new KeyGenerator(){
            @Override
            public Object generate(Object o, Method method, Object... objects) {
                return null;
            }
        };
    }


}
