package test.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解： 1. JDK中预定义的一些注解
 * 
 * @Override：检测被该注解标注的方法是否是继承自父类（接口）的
 * @Deprecated：该注解标注的方法表示该方法已过时
 * @SuppressWarnings:压制警告的注解,一般传递的参数为all，如：@SuppressWarnings("all")
 *
 *                                                                  2. 自定义注解 2.1
 *                                                                  格式：
 *                                                                  public @interface
 *                                                                  注解名称{ 属性列表;
 *                                                                  }
 *                                                                  本质：注解本质上使用一个接口，该接口默认继承Annotation接口
 *
 *                                                                  2.2
 *                                                                  属性：接口中的抽象方法
 *                                                                  要求：
 *                                                                  属性（抽象方法）的返回值类型需要是以下几种类型
 *                                                                  （1）基本数据类型
 *                                                                  （2）String
 *                                                                  （3）枚举 （4）注解
 *                                                                  （5）以上类型的数组
 *                                                                  定义了属性，在使用时需要给属性进行赋值
 *                                                                  （1）如果定义属性时，使用default关键字给属性默认的初始值，则使用注解时，可以不进行属性的赋值
 *                                                                  （2）如果只有一个属性需要赋值，且属性的名称是value，则value可以省略，直接定义值即可。
 *                                                                  （3）数组赋值时，值使用{}包裹，如果数组汇总只有一个值，则{}可以省略
 *                                                                  3.
 *                                                                  元注解：用于描述注解的注解
 * @Target：描述注解能够作用的位置，如：类、方法等 ElementType取值： （1）TYPE:可以作用在类上 （2）METHOD:可以作用在方法上
 *                             （3）FIELD:可以作用在成员变量上
 * @Retention：描述注解被保留的阶段 RetentionPolicy取值
 * @Retention(RetentionPolicy.RUNTIME) （1）RUNTIME:一般自定义的注解取RUNTIME即可，表示表描述的注解作用于运行时阶段
 * @Documented:描述注解是否被抽取到api文档中
 * @Inherited:描述注解是否会被子类继承 4. 在程序中使用注解：主要是为了获取注解找那个定义的属性值 注解在框架里一般都是用来替换配置文件的
 */

@Pro(className = "test.annotation.bean.person", methodName = "eat")
public class AnnotationDemo {
	public static void main(String[] args) throws Exception {

		// 获取该类的字节码文件
		Class demoClass = AnnotationDemo.class;
		// 获取该类产生的字节码文件上的注解对象
		Pro p = (Pro) demoClass.getAnnotation(Pro.class);// 这行代码相当于在内存中生产了一个注解接口的子类实现对象，相当于如下代码：
		@SuppressWarnings("unused")
		class proImpl implements Pro {

			public String className() {
				return "test.annotation.bean.person";
			}

			public String methodName() {
				return "eat";
			}

			public Class<? extends Annotation> annotationType() {
				return null;
			}

		}
		// 利用获取的注解对象，获取对象中的属性（抽象方法）
		String className = p.className();
		String methodName = p.methodName();

		System.out.println(" className =  " + className);
		System.out.println(" methodName =  " + methodName);

		// 利用反射机制根据类名获取类对象
		Class cls = Class.forName(className);
		// 利用类对象中的newInstance方法创建对象
		Object o = cls.newInstance();
		// 利用反射中getMethod方法获取指定方法名的方法
		Method method = cls.getMethod(methodName);
		// 调用invoke方法执行对象中的method方法
		method.invoke(o);
	}
}
