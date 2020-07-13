package com.amazon.ask.helloworld.database;

public class Tool {
	public static String firstUpperCase(String str) {
		if(str.length() < 1) return str;
		String remain = str.substring(1, str.length());
		String first = str.substring(0, 1);
		first = first.toUpperCase();
		return first + remain;
	}
	
	public static void main(String[] args) {
		String t = firstUpperCase("hello my dear");
		System.out.println(t);
	}

}
