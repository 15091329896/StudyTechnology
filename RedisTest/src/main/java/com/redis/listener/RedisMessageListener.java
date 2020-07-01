package com.redis.listener;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/*** imports ***/
@SuppressWarnings("rawtypes")
public class RedisMessageListener implements MessageListener {
	private RedisTemplate redisTemplate;

	/*** 此处省略redisTemplate的 setter和getter方法 ***/
	@Override
	public void onMessage(Message message, byte[] bytes) {
		// 获取消息
		byte[] body = message.getBody();
		// 使用值序列化器转换
		String msgBody = (String) getRedisTemplate().getValueSerializer().deserialize(body);
		System.out.println(msgBody);
		// 获取 channel
		byte[] channel = message.getChannel();
		// 使用字符串序列化器转换
		String channelStr = (String) getRedisTemplate().getStringSerializer().deserialize(channel);
		System.out.println(channelStr);
		// 渠道名称转换
		String bytesStr = new String(bytes);
		System.out.println(bytesStr);
	}

	public RedisTemplate getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}