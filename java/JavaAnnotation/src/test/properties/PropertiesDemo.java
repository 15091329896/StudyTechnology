package test.properties;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

public class PropertiesDemo {
	public static void main(String[] args) throws Exception {
		//创建properties对象
		Properties pro = new Properties();

		//通过本类的类加载器将properties文件中的内容加载到pro对象中
		ClassLoader classLoader = PropertiesDemo.class.getClassLoader();
		InputStream is = classLoader.getResourceAsStream("test/properties/pro.properties");
		pro.load(is);

		//利用properties对象中getProperty方法利用key获取value
		String className = pro.getProperty("className");
		String methodName = pro.getProperty("methodName");

		System.out.println(" className =  " + className);
		System.out.println(" methodName =  " + methodName);
		
		
		//利用反射机制根据类名获取类对象
		Class cls = Class.forName(className);
		//利用类对象中的newInstance方法创建对象
		Object o = cls.newInstance();
		//利用反射中getMethod方法获取指定方法名的方法
		Method method = cls.getMethod(methodName);
		//调用invoke方法执行对象中的method方法
		method.invoke(o);


	}

}
