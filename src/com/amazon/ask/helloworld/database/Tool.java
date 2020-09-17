package com.amazon.ask.helloworld.database;

/**
 * A class for possible tool methods. 
 * @author YirongLi
 *
 */
public class Tool {
	
	/**
	 * Method that turns the first letter in a sentence to upper case.
	 * @param str - a sentence
	 * @return the sentence with capital letter in upper case
	 */
	public static String firstUpperCase(String str) {
		if(str.length() < 1) return str;
		String remain = str.substring(1, str.length());
		String first = str.substring(0, 1);
		first = first.toUpperCase();
		return first + remain;
	}

}
