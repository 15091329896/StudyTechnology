package com.redis.util;
import org.springframework.data.redis.core.RedisTemplate; 
import org.springframework.data.redis.serializer.RedisSerializer;
/**
 * redis发布工具类
 * @author ts
 * @version 2019-07-16
 */
public class RedisPuUtil {
	
	 private RedisTemplate<String, Object> redisTemplate = null;
 
	    
	    public void sendMessage(String channel,  RedisSerializer  message) {
	        redisTemplate.convertAndSend(channel, message);
	    }
 
 
	    public RedisTemplate getRedisTemplate() {
	        return redisTemplate;
	    }
 
	    public void setRedisTemplate(RedisTemplate redisTemplate) {
	        this.redisTemplate = redisTemplate;
	    }
}
