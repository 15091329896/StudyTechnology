package test.annotation.bean;

public class person {

	private String name;
	private int age;

	public person() {
	}

	public person(String name) {
		this.name = name;
	}

	public person(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "person{" + "name='" + name + '\'' + ", age=" + age + '}';
	}

	public void eat() {
		System.out.println("调用eat方法！");
	}
}
