package com.spring.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.spring.inte.PersonDao;

public class TestSpring {

	@Test
	public void testl() {
		String xmlPath = "applicationContext.xml";
		// 初始化Spring容器，加载配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(xmlPath);
		// 通过容器获取personDao实例
		PersonDao personDao = (PersonDao) applicationContext.getBean("personDao");
		// 调用 personDao 的 add ()方法
		personDao.add();
	}

	@Test
	public void test2() {
		System.out.println("***************test2***************");
	}
}
