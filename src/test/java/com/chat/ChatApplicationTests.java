package com.chat;

import com.chat.common.SystemConstant;
import com.chat.domain.AddInfo;
import com.chat.domain.GroupNumber;
import com.chat.entity.Add;
import com.chat.entity.User;
import com.chat.repository.UserMapper;
import com.chat.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest
public class ChatApplicationTests {

	@Autowired
	RedisService redisService;

	@Autowired
	RedisTemplate redisTemplate;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Test
	public void contextLoads() {
		redisService.setSet(SystemConstant.ONLINE_USER,"109");
    }

    @Test
    public void test(){
		stringRedisTemplate.opsForValue().append("msg","hello");
	}
}
