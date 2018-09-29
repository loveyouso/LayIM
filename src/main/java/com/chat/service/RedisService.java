package com.chat.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @description redis相关操作
 * @author loveyouso
 * @create 2018-09-19 17:03
 **/
@Service
public class RedisService {

    private Logger LOGGER = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    /**
     * @description 获取Set集合数据
     * @param k
     * @return Set[String]
     */
    public Set<String> getSets(String k){
        return redisTemplate.opsForSet().members(k);
    }

    /**
     * @description 移除Set集合中的value
     * @param k
     * @param v
     */
    public void removeSetValue(String k,String v){
        if (k == null && v == null){
            return;
        }
        redisTemplate.opsForSet().remove(k, v);
    }

    /**
     * @description 保存到Set集合中
     * @param k
     * @param v
     */
    public void setSet(String k,String v){
        if (k == null && v == null){
            System.out.println("key:" + k + " " + "value:" + v);
        }
        redisTemplate.opsForSet().add(k,v);
    }

    /**
     * @description 存储Map格式
     * @param key
     * @param hashKey
     * @param hashValue
     *
     */
    public void setMap(String key,String hashKey,String hashValue){
        redisTemplate.opsForHash().put(key,hashKey,hashValue);
    }

    /**
     * @description 存储带有过期时间的key-value
     * @param key
     * @param value
     * @param timeOut 过期时间
     * @param unit 时间单位
     *
     */
    public void setTime(String key,String value,Long timeOut,TimeUnit unit){
        if (value == null){
            LOGGER.info("redis存储的value的值为空");
            throw new IllegalArgumentException("redis存储的value的值为空");
        }
        if (timeOut > 0){
            redisTemplate.opsForValue().set(key,value,timeOut,unit);
        }else{
            redisTemplate.opsForValue().set(key,value);
        }
    }

    /**
     * @description 存储key-value
     * @param key
     * @return Object
     *
     */
    public void set(String key,String value){
        if (value == null){
            LOGGER.info("redis存储的value的值为空");
            throw new IllegalArgumentException("redis存储的value的值为空");
        }
        redisTemplate.opsForValue().set(key,value);
    }

    /**
     * @description 根据key获取value
     * @param key
     * @return Object
     *
     */
    public Object get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * @description 判断key是否存在
     * @param key
     * @return Boolean
     *
     */
    public boolean exists(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * @description 删除key对应的value
     * @param key
     *
     */
    public void removeValue(String key){
        if (exists(key)){
            redisTemplate.delete(key);
        }
    }

    /**
     * @description 模式匹配批量删除key
     * @param keyParttern
     *
     */
    public void removePattern(String keyParttern){
        Set<String> keys = redisTemplate.keys(keyParttern);
        if (keys.size()> 0){
            redisTemplate.delete(keys);
        }
    }

}

