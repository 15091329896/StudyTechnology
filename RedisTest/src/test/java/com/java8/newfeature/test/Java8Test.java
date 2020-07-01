package com.java8.newfeature.test;


public class Java8Test {
	public static void main(String[] args) {
		GreetingService greetService1 = message ->{
			 System.out.println("Hello" + message);
		};
		
		greetService1.sayMessage("jiaa");
		
	}
	
	
}
