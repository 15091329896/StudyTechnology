package com.spring.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;

import com.springredis.bean.Role;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;


@SuppressWarnings({ "rawtypes", "unchecked", "resource", "unused" })
public class SaveObject {

	@Test
	public void testSaveObject() {
		// 测试保存这个 Role 对象了，测试代码如下所示。

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		Role role = new Role();
		role.setId(1001L);
		role.setRoleName("李白");
		role.setNote("李白是诗人");
		redisTemplate.opsForValue().set("role_1", role);
		Role role1 = (Role) redisTemplate.opsForValue().get("role_1");
		System.out.println(role1.toString());

	}

	@Test
	public void testSaveObjectInOneSession() {
		// 使用 SessionCallback 这个接口，通过这个接口就可以把多个命令放入到同一个 Redis 连接中去执行
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate<String, Role> redisTemplate = applicationContext.getBean(RedisTemplate.class);
		final Role role = new Role();
		role.setId(1002L);
		role.setRoleName("Linux");
		role.setNote("李白是突厥人");
		SessionCallback callBack = new SessionCallback<Role>() {

			public Role execute(RedisOperations ops) throws DataAccessException {
				// 插入数据库
				ops.boundValueOps("role_1").set(role);
				// 查询数据库
				return (Role) ops.boundValueOps("role_1").get();
			}
		};
		Role savedRole = (Role) redisTemplate.execute(callBack);
		System.out.println(savedRole.toString());

	}

	@Test
	public void test3() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		// 设值
		ValueOperations opsForValue = redisTemplate.opsForValue();
		opsForValue.set("key1", "value1");
		opsForValue.set("key2", "value2");
		// 通过key获取值
		String value1 = (String) opsForValue.get("key1");
		System.out.println(value1);
		// 通过key删除值
		redisTemplate.delete("key1");
		// 求长度
		Long length = opsForValue.size("key2");
		System.out.println(length);
		// 设值新值并返回旧值
		String oldValue2 = (String) opsForValue.getAndSet("key2", "new_value2");
		System.out.println(oldValue2);
		// 通过key获取值.
		String value2 = (String) opsForValue.get("key2");
		System.out.println(value2);
		// 求子串
		String rangeValue2 = opsForValue.get("key2", 0, 3);
		System.out.println(" rangeValue2 = " + rangeValue2);
		// 追加字符串到末尾，返回新串长度
		String appendBrfore = (String) opsForValue.get("key2");
		System.out.println(" appendBrfore = " + appendBrfore);
		int newLen = opsForValue.append("key2", "_app");
		System.out.println(" newLen = " + newLen);
		String appendValue2 = (String) opsForValue.get("key2");
		System.out.println(" appendValue2 = " + appendValue2);
	}

	/**
	 * 测试Redis运算.
	 */
	@Test
	public void testCal() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		redisTemplate.opsForValue().set("i", "9");
		System.out.println(((String) redisTemplate.opsForValue().get("i")).toString());
		redisTemplate.opsForValue().increment("i", 1);
		System.out.println(((String) redisTemplate.opsForValue().get("i")).toString());
		redisTemplate.getKeySerializer().serialize("i");
		System.out.println(((String) redisTemplate.opsForValue().get("i")).toString());
		redisTemplate.getConnectionFactory().getConnection().decrBy(redisTemplate.getKeySerializer().serialize("i"), 6);
		System.out.println(((String) redisTemplate.opsForValue().get("i")).toString());
		redisTemplate.opsForValue().increment("i", 2.3);
		System.out.println(((String) redisTemplate.opsForValue().get("i")).toString());
	}

	@Test
	public void testCal2() {
		/*
		 * 测试redis运算 初始化spring容器 获取RedisTemplate 实例 新增数据 加1 减4 加2.3
		 */

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		redisTemplate.opsForValue().set("i", "9");
		view1001(redisTemplate);
		// 加1
		redisTemplate.opsForValue().increment("i", 1);
		view1001(redisTemplate);
		// 减4
		redisTemplate.opsForValue().decrement("i", 4);
		view1001(redisTemplate);
		redisTemplate.opsForValue().increment("i", 2.3);
		view1001(redisTemplate);

	}

	private void view1001(RedisTemplate redisTemplate) {
		System.out.println(redisTemplate.opsForValue().get("i").toString());
	}

	/**
	 * 打印当前key的值
	 * 
	 * @param redisTemplate
	 *            spring RedisTemplate
	 * @param key
	 *            键
	 */
	// @Test
	// public static void printCurrValue(RedisTemplate redisTemplate, String
	// key) {
	// String i = (String) redisTemplate.opsForValue().get(key);
	// System.err.println(i);
	// }

	// @Test
	private void test5() {
		System.out.println(" test5 被调用 ");
	}

	@Test
	public void testRedisHash() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		String key = "hash";
		Map<String, String> map = new HashMap<String, String>();
		map.put("f1", "val1");
		map.put("f2", "val2");
		// 相当于hmset命令
		redisTemplate.opsForHash().putAll(key, map);
		// 相当于hset命令
		redisTemplate.opsForHash().put(key, "f3", "6");
		printValueForhash(redisTemplate, key, "f3");
		// 相当于 hexists key filed 命令
		boolean exists = redisTemplate.opsForHash().hasKey(key, "f3");
		System.out.println(exists);
		// 相当于hgetall命令
		Map keyValMap = redisTemplate.opsForHash().entries(key);
		// 相当于hincrby命令
		redisTemplate.opsForHash().increment(key, "f3", 2);
		printValueForhash(redisTemplate, key, "f3");
		// 相当于hincrbyfloat命令
		redisTemplate.opsForHash().increment(key, "f3", 0.88);
		printValueForhash(redisTemplate, key, "f3");
		// 相当于hvals命令
		List valueList = redisTemplate.opsForHash().values(key);
		// 相当于hkeys命令
		Set keyList = redisTemplate.opsForHash().keys(key);
		List<String> fieldList = new ArrayList<String>();
		fieldList.add("f1");
		fieldList.add("f2");
		// 相当于hmget命令
		List valueList2 = redisTemplate.opsForHash().multiGet(key, keyList);
		// 相当于hsetnx命令
		boolean success = redisTemplate.opsForHash().putIfAbsent(key, "f4", "val4");
		System.out.println(success);
		// 相当于hdel命令
		Long result = redisTemplate.opsForHash().delete(key, "fl", "f2");
		System.out.println(result);
	}

	@Test
	public void testRedisHash2() {
		/*
		 * 得到容器 得到redistemplate的实例
		 * 
		 */
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		String key = "hashTest";
		Map<String, String> map = new HashMap<String, String>();
		map.put("f1", "val1");
		map.put("f2", "val2");
		HashOperations opsForHash = redisTemplate.opsForHash();
		opsForHash.putAll(key, map);
		// 修改操作
		opsForHash.put(key, "f3", "6");
		viewHash(key, opsForHash, "f1");
		System.out.println(opsForHash.hasKey(key, "f3"));
		System.out.println(opsForHash.hasKey(key, "f4"));
		viewAll(key, opsForHash);
		// 加2
		opsForHash.increment(key, "f3", 2);
		viewHash(key, opsForHash, "f3");
		// 加0.88
		opsForHash.increment(key, "f3", 0.88);
		viewHash(key, opsForHash, "f3");
		// 获取所有的值 val
		List<String> values = opsForHash.values(key);
		for (String string : values) {
			System.out.println(string);
		}
		// 获取所有的键 key
		Set<String> keys = opsForHash.keys(key);
		for (String string : keys) {
			System.out.println(string);
		}

		// 指定多个键，获取对应的值
		List<String> fileList = new ArrayList<String>();
		fileList.add("f1");
		fileList.add("f2");
		List<String> multiGet = opsForHash.multiGet(key, fileList);
		for (String string : fileList) {
			System.out.println(string);
		}
		for (String string : multiGet) {
			System.out.println(string);
		}
		viewAll(key, opsForHash);
		// 修改操作
		redisTemplate.opsForHash().put(key, "f1", "val1f1");
		System.out.println("--------");
		// 在hashkey不存在的情况下新增
		Boolean putIfAbsent = opsForHash.putIfAbsent(key, "f5", "valf5");
		System.out.println(putIfAbsent);
		viewAll(key, opsForHash);

	}

	private void viewAll(String key, HashOperations opsForHash) {
		Map entries = opsForHash.entries(key);
		for (Object keyString : entries.keySet()) {
			System.out.println(keyString + " : " + entries.get(keyString));
		}
	}

	private void viewHash(String key, HashOperations opsForHash, String hashKey) {
		System.out.println(opsForHash.get(key, hashKey).toString());
	}

	private static void printValueForhash(RedisTemplate redisTemplate, String key, String field) {
		// 相当于hget命令
		Object value = redisTemplate.opsForHash().get(key, field);
		System.out.println(value);
	}

	@Test
	public void testList() {
		ApplicationContext applicationcontext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationcontext.getBean(RedisTemplate.class);
		String listName = "list";
		try {
			// 删除链表，以便我们可以反复测试
			redisTemplate.delete(listName);
			// 把node3插入链表list
			redisTemplate.opsForList().leftPush(listName, "node3");
			List<String> nodeList = new ArrayList<String>();
			for (int i = 2; i >= 1; i--) {
				nodeList.add("nnode" + i);
			}
			printList(redisTemplate, listName);
			System.out.println("// 相当于lpush把多个值从左插入链表");
			redisTemplate.opsForList().leftPushAll(listName, nodeList);
			printList(redisTemplate, listName);
			System.out.println("// 从右边插入一个节点");
			redisTemplate.opsForList().rightPush(listName, "node4");
			printList(redisTemplate, listName);
			System.out.println("// 获取下标为0的节点");
			String nodel = (String) redisTemplate.opsForList().index("list", 0);
			printList(redisTemplate, listName);
			System.out.println("// 获取链表长度");
			long size = redisTemplate.opsForList().size("listn");
			System.out.println(size);
			printList(redisTemplate, listName);
			System.out.println("// 从左边弹出一个节点");
			String lpop = (String) redisTemplate.opsForList().leftPop(listName);
			System.out.println("-->" + lpop);
			printList(redisTemplate, listName);
			System.out.println("// 从右边弹出一个节点");
			String rpop = (String) redisTemplate.opsForList().rightPop(listName);
			System.out.println("-->" + rpop);
			// 注意，需要使用更为底层的命令才能操作linsert命令
			printList(redisTemplate, listName);
			System.out.println("// 使用linsert命令在node2前插入一个节点");
			redisTemplate.getConnectionFactory().getConnection().lInsert(listName.getBytes("utf-8"),
					RedisListCommands.Position.BEFORE, "node2".getBytes("utf-8"), "before_node".getBytes("utf-8"));
			printList(redisTemplate, listName);
			System.out.println("// 使用linsert命令在node2后插入一个节点");
			redisTemplate.getConnectionFactory().getConnection().lInsert(listName.getBytes("utf-8"),
					RedisListCommands.Position.AFTER, "node2".getBytes("utf-8"), "after_node".getBytes("utf-8"));
			printList(redisTemplate, listName);
			System.out.println("// 判断list是否存在，如果存在则从左边插入head节点");
			redisTemplate.opsForList().leftPushIfPresent(listName, "head");
			printList(redisTemplate, listName);
			System.out.println("// 判断list是否存在，如果存在则从右边插入end节点");
			redisTemplate.opsForList().rightPushIfPresent(listName, "end");
			printList(redisTemplate, listName);
			System.out.println("// 从左到右，或者下标从0到10的节点元素");
			List valueList = redisTemplate.opsForList().range(listName, 0, 10);
			nodeList.clear();
			for (int i = 1; i <= 3; i++) {
				nodeList.add("node");
			}
			printList(redisTemplate, listName);
			System.out.println("// 在链表左边插入三个值为node的节点");
			redisTemplate.opsForList().leftPushAll(listName, nodeList);
			printList(redisTemplate, listName);
			System.out.println("// 从左到右删除至多三个node节点");
			redisTemplate.opsForList().remove(listName, 3, "node");
			printList(redisTemplate, listName);
			System.out.println("// 给链表下标为0的节点设置新值");
			redisTemplate.opsForList().set(listName, 0, "new_head_value");
			printList(redisTemplate, listName);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		// 打印链表数据
		printList(redisTemplate, listName);
	}

	@Test
	public void testList2() throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		String key = "list";
		ListOperations opsForList = redisTemplate.opsForList();
		// 删除链表
		System.out.println("删除链表...");
		redisTemplate.delete(key);
		// 加入node3到链表中
		System.out.println("从左边插入node3到链表中");
		opsForList.leftPush(key, "node3");
		List<String> nodeList = new ArrayList<String>();
		for (int i = 2; i >= 1; i--) {
			nodeList.add("node" + i);
		}
		printList(redisTemplate, key);
		System.out.println("将多个值从左侧插入链表...");
		opsForList.leftPushAll(key, nodeList);
		printList(redisTemplate, key);
		System.out.println("从右边插入一个节点");
		opsForList.rightPush(key, "node4");
		printList(redisTemplate, key);
		System.out.println("获取链表长度");
		Long size = opsForList.size(key);
		System.out.println("-->" + size);
		System.out.println("从左边弹出一个节点");
		Object leftPop = opsForList.leftPop(key);
		System.out.println(leftPop.toString());
		System.out.println("从右边弹出一个节点");
		Object rightPop = opsForList.rightPop(key);
		System.out.println(rightPop.toString());
		System.out.println("使用linsert插入一个节点在node2之前");
		printList(redisTemplate, key);
		System.out.println("插入后：");
		// 操作失败

		redisTemplate.getConnectionFactory().getConnection().lInsert(key.getBytes("utf-8"),
				RedisListCommands.Position.BEFORE, "node2".getBytes("utf-8"), "after_node".getBytes("utf-8"));
		printList(redisTemplate, key);
		System.out.println("使用linsert插入一个节点在node2之后");
		System.out.println("插入后：");
		// 操作失败
		redisTemplate.getConnectionFactory().getConnection().lInsert(key.getBytes("utf-8"),
				RedisListCommands.Position.AFTER, "node2".getBytes("utf-8"), "after_node".getBytes("utf-8"));
		printList(redisTemplate, key);
		System.out.println(" 判断list是否存在，如果存在则从左边插入head节点 ");
		opsForList.leftPushIfPresent(key, "head");
		printList(redisTemplate, key);
		System.out.println(" 判断list是否存在，如果存在则从左边插入head节点 ");
		opsForList.rightPushIfPresent(key, "end");
		System.out.println("得到从左到右下标从0到10的节点元素");
		List range = opsForList.range(key, 0, 10);
		for (Object object : range) {
			System.out.println(object.toString());
		}
		nodeList.clear();
		for (int i = 0; i < 3; i++) {
			nodeList.add("node");
		}
		System.out.println("在链表左边插入三个值为node的节点");
		Long leftPushAll = opsForList.leftPushAll(key, nodeList);
		System.out.println(leftPushAll);
		printList(redisTemplate, key);
		System.out.println("从左到右删除至多三个值为node的节点");
		Long remove = opsForList.remove(key, 0, "node");
		System.out.println("remove = " + remove);
		System.out.println("给链表下标为0的节点设置新值");
		opsForList.set(key, 0, "new_head_value");
		printList(redisTemplate, key);

	}

	@Test
	public void testBList() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		// 清空数据，可以重复测试
		redisTemplate.delete("list1");
		redisTemplate.delete("list2");
		// 初始化链表 list1
		List<String> nodeList = new ArrayList<String>();
		for (int i = 1; i <= 5; i++) {
			nodeList.add("node" + i);
		}
		redisTemplate.opsForList().leftPushAll("list1", nodeList);
		// Spring 使用参数超时时间作为阻塞命令区分，等价于 blpop 命令，并且可以设置时间参数
		// redisTemplate.opsForList().leftPop ("list1", 1, TimeUnit.SECONDS);
		// Spring 使用参数超时时间作为阻塞命令区分，等价于 brpop 命令，并且可以设置时间参数
		redisTemplate.opsForList().rightPop("list1", 1, TimeUnit.SECONDS);
		redisTemplate.opsForList().rightPop("list1");

		nodeList.clear();
		// 初始化链表 list2
		for (int i = 1; i <= 3; i++) {
			nodeList.add("dato" + i);
		}
		redisTemplate.opsForList().leftPushAll("list2", nodeList);
		// 相当于 rpoplpush 命令，弹出 list1 最右边的节点，插入到 list2 最左边
		// redisTemplate.opsForList().rightPopAndLeftPush("list1", "list2",
		// 1,TimeUnit.SECONDS);
		// 相当于brpoplpush命令，注意在 Spring 中使用超时参数区分
		redisTemplate.opsForList().rightPopAndLeftPush("list1", "list2", 1, TimeUnit.SECONDS);
		// 打印链表数据
		printList(redisTemplate, "list1");
		printList(redisTemplate, "list2");
	}

	@Test
	public void testBList2() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		ListOperations opsForList = redisTemplate.opsForList();
		// 清空数据
		String list1 = "list1";
		String list2 = "list2";
		redisTemplate.delete(list1);
		redisTemplate.delete(list2);
		List<String> nodeList = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			nodeList.add("List1_node" + i);
		}
		opsForList.rightPushAll(list1, nodeList);
		nodeList.clear();
		for (int i = 0; i < 10; i++) {
			nodeList.add("List2_node" + i);
		}
		opsForList.rightPushAll(list2, nodeList);
		printList(redisTemplate, list1);
		printList(redisTemplate, list2);

		// opsForList.leftPop(list1);
		new Thread(new Thread1("线程1", opsForList, list1, list2)).start();
		new Thread(new Thread1("线程2", opsForList, list1, list2)).start();
		new Thread(new Thread1("线程3", opsForList, list1, list2)).start();

	}

	public void printList(RedisTemplate redisTemplate, String key) {
		// 链表长度
		Long size = redisTemplate.opsForList().size(key);
		List<String> valueList = redisTemplate.opsForList().range(key, 0, size);
		for (String string : valueList) {
			System.out.print(string + " ");
		}
		System.out.println();
	}

	static class Thread1 implements Runnable {
		private ListOperations opsForList = null;
		private String listName1 = null;
		private String listName2 = null;
		private String threadName = null;

		// 测试加锁的leftpop
		public synchronized void run() {
			for (int i = 0; i < 10; i++) {
				System.out.println("这是--->" + threadName + " 在操作  " + listName1 + " 和  " + listName2);
				printList(opsForList, listName1);
				printList(opsForList, listName2);
				opsForList.rightPopAndLeftPush(listName1, listName2, 3, TimeUnit.SECONDS);
				System.out.println("操作后");
				printList(opsForList, listName1);
				printList(opsForList, listName2);
			}
		}

		public void printList(ListOperations opsForList, String key) {
			// 链表长度
			Long size = opsForList.size(key);
			List<String> valueList = opsForList.range(key, 0, size);
			System.out.print(threadName + " " + key + " ");
			for (String string : valueList) {
				System.out.print(string + " ");
			}
			System.out.println();
		}

		public Thread1(String threadNameInput, ListOperations opsForListInput, String listName1Inpout,
				String listName2Inpout) {
			this.opsForList = opsForListInput;
			this.listName1 = listName1Inpout;
			this.listName2 = listName2Inpout;
			this.threadName = threadNameInput;
		}

		public ListOperations getOpsForList() {
			return opsForList;
		}

		public void setOpsForList(ListOperations opsForList) {
			this.opsForList = opsForList;
		}

		public String getListName1() {
			return listName1;
		}

		public void setListName1(String listName1) {
			this.listName1 = listName1;
		}

		public String getListName2() {
			return listName2;
		}

		public void setListName2(String listName2) {
			this.listName2 = listName2;
		}

		public String getThreadName() {
			return threadName;
		}

		public void setThreadName(String threadName) {
			this.threadName = threadName;
		}

	}

	@Test
	public void testRedisSet() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		SetOperations opsForSet = redisTemplate.opsForSet();
		Set set = null;
		String set1 = "set1";
		String set2 = "set2";
		// 将元素加入列表
		redisTemplate.boundSetOps(set1).add("vl", "v2", "v3", "v4", "v5", "v6");
		redisTemplate.boundSetOps(set2).add("v0", "v2", "v4", "v6", "v8");
		printSet(opsForSet, "set1");
		printSet(opsForSet, "set2");
		// 求集合长度
		opsForSet.size(set1);
		// 求差集
		set = opsForSet.difference(set1, set2);
		printSet(set);
		// 求并集
		set = opsForSet.intersect(set1, set2);
		printSet(set);
		// 判断是否集合中的元素
		boolean exists = opsForSet.isMember(set1, "vl");
		System.out.println(exists);
		// 获取集合所有元素
		set = opsForSet.members(set1);
		printSet(set);
		// 从集合中随机弹出一个元素
		String val = (String) opsForSet.pop(set1);
		System.out.println(val);
		// 随机获取一个集合的元素
		val = (String) opsForSet.randomMember(set1);
		// 随机获取2个集合的元素
		List list = opsForSet.randomMembers(set1, 2L);
		for (Object object : list) {
			System.out.println(object.toString());
		}
		// 删除一个集合的元素，参数可以是多个
		Long remove = opsForSet.remove(set1, "v1");
		System.out.println("opsForSet.remove(set1, \"v1\") = " + remove);
		// 求两个集合的并集
		Set union = opsForSet.union(set1, set2);
		printSet(union);
		// 求两个集合的差集，并保存到集合diff_set中
		Long differenceAndStore = opsForSet.differenceAndStore(set1, set2, "diff_set");
		printSet(opsForSet, "diff_set");
		// 求两个集合的交集，并保存到集合inter_set中
		opsForSet.intersectAndStore(set1, set2, "inter_set");
		printSet(opsForSet, "inter_set");
		// 求两个集合的并集，并保存到集合union_set中
		opsForSet.unionAndStore(set1, set2, "union_set");
		printSet(opsForSet, "union_set");
	}

	@Test
	public void testRedisSet2() {
		/*
		 * 测试set
		 */
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		SetOperations opsForSet = redisTemplate.opsForSet();
		String list1 = "list1";
		String list2 = "list2";
		redisTemplate.boundSetOps(list1).add("v1", "v2", "v3", "v4", "v5", "v6");

	}

	private void printSet(Set set) {
		for (Object object : set) {
			System.out.println(object.toString());
		}
	}

	private void printSet(SetOperations opsForSet, String key) {
		Set set = opsForSet.members(key);
		for (Object object : set) {
			System.out.println(object.toString());
		}
	}

	/**
	 * 测试基数集合
	 */
	@Test
	public void testRedisHyperLogLog() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		redisTemplate.opsForHyperLogLog().add("HyperLogLog", "a", "b", "c", "d", "a");
		redisTemplate.opsForHyperLogLog().add("HyperLogLog2", "a");
		redisTemplate.opsForHyperLogLog().add("HyperLogLog2", "z");
		Long size = redisTemplate.opsForHyperLogLog().size("HyperLogLog");
		System.err.println(size);
		size = redisTemplate.opsForHyperLogLog().size("HyperLogLog2");
		System.err.println(size);
		redisTemplate.opsForHyperLogLog().union("des_key", "HyperLogLog", "HyperLogLog2");
		size = redisTemplate.opsForHyperLogLog().size("des_key");
		System.err.println(size);
	}

	@Test
	public void testRedisTransaction() {

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		SessionCallback callBack = (SessionCallback) (RedisOperations ops) -> {
			ops.multi();
			ops.boundValueOps("key1").set("value1");
			// 注意由于命令只是进入队列，而没有被执行，所以此处采用get命令，而value却返回为null
			String value = (String) ops.boundValueOps("key1").get();
			System.out.println("事务执行过程中，命令入队列，而没有被执行，所以value为空： value=" + value);
			// 此时list会保存之前进入队列的所有命令的结果
			List list = ops.exec(); // 执行事务
			// 事务结束后，获取value1
			value = (String) redisTemplate.opsForValue().get("key1");
			return value;
		};
		// 执行Redis的命令
		String value = (String) redisTemplate.execute(callBack);
		System.out.println(value);

	}
	
	@Test
	public void testRedisTransaction2() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
		SessionCallback callback = (SessionCallback) (RedisOperations ops) -> {
			ops.multi();
			ops.boundValueOps("key1").set("value1");
			String value = (String) ops.boundValueOps("key1").get();
			System.out.println("事务执行过程中，命令入队列，而没有被执行，所以value为空： value=" + value);
//			执行事务
			List list = ops.exec();
			for (Object object : list) {
				System.out.println(" ops.exec() =  " + object.toString());
			}
			value = (String) redisTemplate.opsForValue().get("key1");
			System.out.println("value = " + value);
			return value;
		};
		String execute = (String) redisTemplate.execute(callback);
		System.out.println(execute);
	}
	
	/*
	 * 测试Redis的流水线机制
	 */
	@Test
	public void testRedisPipelinedByJedis() {
		
		JedisPool JEDIS_POOL = null;
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(500); // 500个连接
		poolConfig.setMaxIdle(32); // 最大的空闲连接
		poolConfig.setMaxWaitMillis(100 * 1000); // 最长的等待时间
		poolConfig.setTestOnBorrow(true); // 获得一个jedis连接时检测可用性
		JEDIS_POOL = new JedisPool(poolConfig, "127.0.0.1", 6379);
	    Jedis jedis = JEDIS_POOL.getResource();
	    long start = System.currentTimeMillis();
	    // 开启流水线
	    Pipeline pipeline = jedis.pipelined();
	    // 这里测试10万条的读/写2个操作
	    for (int i = 0; i < 100000; i++) {
	        int j = i + 1;
	        pipeline.set("pipeline_key_" + j, "pipeline_value_" + j);
	        pipeline.get("pipeline_key_" + j);
	    }
	    // pipeline.sync(); //这里只执行同步，但是不返回结果
	    // pipeline.syncAndReturnAll ();将返回执行过的命令返回的List列表结果
	    List result = pipeline.syncAndReturnAll();
	    long end = System.currentTimeMillis();
	    // 计算耗时
	    System.err.println("耗时：" + (end - start) + "毫秒");
	}
	
	
	@Test
	public void testRedisPipelinedBySpring() {
	    ApplicationContext applicationcontext = new ClassPathXmlApplicationContext("applicationcontext.xml");
	    RedisTemplate redisTemplate = applicationcontext.getBean(RedisTemplate.class);
	    // 使用Java8的Lambda表达式
	    SessionCallback callBack = (SessionCallback) (RedisOperations ops)-> {
	        for (int i = 0; i<100000; i++)    {
	            int j = i + 1;
	            ops . boundValueOps ("pipeline_key_" + j ).set("piepeline_value_"+j);
	            ops.boundValueOps("pipeline_key_" + j).get();
	        }
	        return null;
	    };
	    long start = System.currentTimeMillis();
	    //执行Redis的流水线命令
	    List resultList= redisTemplate.executePipelined(callBack);
	    long end = System.currentTimeMillis();
	    System.err.println(end-start);
		
	}
	
	@Test
	public void testRedisPipelined() {
		testRedisPipelinedByJedis();
		testRedisPipelinedBySpring();
		
	}
	
	@Test
	public void testRedisSubscribe() {
	    ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
	    RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
	    String channel = "chat";
	    redisTemplate.convertAndSend(channel, "I am lazy!!");  
	}
	
	
	@Test
    public void testExpire()    {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext ("applicationContext.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
        redisTemplate.execute((RedisOperations ops) -> {
            ops.boundValueOps("key1").set("value1");
            String keyValue = (String)ops.boundValueOps("key1").get();
            getTTL(ops);
            boolean b = false;
            b = ops.expire("key1", 120L, TimeUnit.SECONDS);
            getTTL(ops);
            b = ops.persist("key1");
            getTTL(ops);
            Long l = 0L;
            l = ops.getExpire("key1");
            Long now = System.currentTimeMillis();
            Date date = new Date();
            date.setTime(now + 120000);
            getTTL(ops);
            Boolean expireAt = ops.expireAt("key", date);
            System.out.println(expireAt);
            return null;
        });
    }

	private void getTTL(RedisOperations ops) {
		Long expSecond = ops.getExpire("key1");
		System.out.println(expSecond);
	}
	
	@Test
    public void testLuaFile() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        RedisTemplate redisTemplate = applicationContext.getBean(RedisTemplate.class);
        //读入文件流
        File file = new File("D:\\app\\Redis\\workspace\\lua\\test.lua");
        byte[] bytes = getFileToByte(file);
        Jedis jedis = (Jedis)redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        //发送文件二进制给Redis,这样REdis就会返回shal标识
        byte[] shal = jedis.scriptLoad(bytes);
        //使用返回的标识执行，其中第二个参数2,表示使用2个键
        //而后面的字符串都转化为了二进制字节进行传输
        Object obj = jedis.evalsha(shal,2,  "key1".getBytes(),"key2".getBytes(),"2".getBytes(), "4".getBytes());
        System.out.println("-----------------");
        System.out.println(obj);
    }
    /**
    * 把文件转化为二进制数组
    * @param file 文件
    * return二进制数组
    */
    public byte[] getFileToByte(File file) {
        byte[] by = new byte[(int) file.length()];
        try {
            InputStream is = new FileInputStream(file);
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream(); byte[] bb = new byte[2048];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            by = bytestream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return by;
    }
	
	
	
	
	

}
