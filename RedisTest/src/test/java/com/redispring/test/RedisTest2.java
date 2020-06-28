package com.redispring.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import com.redispring.inte.PersonDao;

public class RedisTest2 {

	@Test
	public void testSpring() {
		String xmlPath = "applicationContext.xml";
		// 初始化Spring容器，加载配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
		// 通过容器获取personDao实例
		PersonDao personDao = (PersonDao) applicationContext.getBean("personDao");
		personDao.add();
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
	@Test
	public void testString() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		// 设值
		redisTemplate.opsForValue().set("key1", "value1");
		redisTemplate.opsForValue().set("key2", "value2");
		// 通过key获取值
		String value1 = (String) redisTemplate.opsForValue().get("key1");
		System.out.println(value1);
		// 通过key删除值
		redisTemplate.delete("key1");
		// 求长度
		Long length = redisTemplate.opsForValue().size("key2");
		System.out.println(length);
		// 设值新值并返回旧值
		String oldValue2 = (String) redisTemplate.opsForValue().getAndSet("key2", "new_value2");
		System.out.println(oldValue2);
		// 通过key获取值.
		String value2 = (String) redisTemplate.opsForValue().get("key2");
		System.out.println(value2);
		// 求子串
		String rangeValue2 = redisTemplate.opsForValue().get("key2", 0, 3);
		System.out.println(rangeValue2);
		// 追加字符串到末尾，返回新串长度
		int newLen = redisTemplate.opsForValue().append("key2", "_app");
		System.out.println(newLen);
		String appendValue2 = (String) redisTemplate.opsForValue().get("key2");
		System.out.println(appendValue2);
	}

	@Test
    public void testRedisHash()  {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        RedisTemplate redisTemplate =  applicationContext.getBean(RedisTemplate.class);
        String key = "hash";
        Map<String, String> map = new HashMap<String,String>();
        map.put("f1", "val1");
        map.put("f2", "val2");
        // 相当于hmset命令
        redisTemplate.opsForHash().putAll(key, map);
        // 相当于hset命令
        redisTemplate.opsForHash().put(key, "f3", "6");
        printValueForhash (redisTemplate, key, "f3");
        // 相当于 hexists key filed 命令
        boolean exists = redisTemplate.opsForHash().hasKey(key, "f3");
        System.out.println(exists);
        // 相当于hgetall命令
        Map keyValMap = redisTemplate.opsForHash().entries(key);
        //相当于hincrby命令
        redisTemplate.opsForHash().increment(key, "f3",2);
        printValueForhash (redisTemplate, key, "f3");
        //相当于hincrbyfloat命令
        redisTemplate.opsForHash().increment (key, "f3", 0.88);
        printValueForhash(redisTemplate, key, "f3");
        //相当于hvals命令
        List valueList = redisTemplate.opsForHash().values(key);
        //相当于hkeys命令
        Set keyList = redisTemplate.opsForHash().keys(key);
        List<String> fieldList = new ArrayList<String>();
        fieldList.add("f1");
        fieldList.add("f2");
        //相当于hmget命令
        List valueList2 = redisTemplate.opsForHash().multiGet(key, keyList);
        //相当于hsetnx命令
        boolean success = redisTemplate.opsForHash () .putIfAbsent(key, "f4", "val4");
        System.out.println(success);
        //相当于hdel命令
        Long result = redisTemplate.opsForHash().delete(key, "fl", "f2");
        System.out.println(result);   
    }
	
    private void printValueForhash(RedisTemplate redisTemplate,String key,String field) {
        //相当于hget命令
        Object value = redisTemplate.opsForHash().get(key,field);
        System.out.println(value);
    }
	
}
