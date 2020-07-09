package test.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述需要执行的类名和方法名
 */
@Target(ElementType.TYPE) // 表示被描述的注解只能作用在类上
@Retention(RetentionPolicy.RUNTIME) // 表示被描述的该注解被使用在运行时
public @interface Pro {
	
	String className();

	String methodName();
}
