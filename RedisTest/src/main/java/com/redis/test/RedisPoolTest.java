package com.redis.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolTest {
	public static void main(String[] args) {
	    JedisPoolConfig poolCfg = new JedisPoolConfig();
	    //最大空闲数
	    poolCfg.setMaxIdle(50);
	    //最大连接数
	    poolCfg.setMaxTotal(100);
	    //最大等待毫秒数
	    poolCfg.setMaxWaitMillis(20000);
	    //使用配置创建连接池
	    JedisPool pool = new JedisPool (poolCfg, "localhost");
	    //从连接池中获取单个连接
	    Jedis jedis = pool.getResource();
	    //如果需密码
	    //jedis.auth("password");
		
		
		
	}
}
