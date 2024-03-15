package com.myway.platform.utils;

import java.util.Random;

public class RandomUtil {

	private static String base = "abcdefghijklmnopqrstuvwxyz0123456789";
	
	public static String randomString(int length){
		StringBuffer value = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int nextInt = random.nextInt(base.length());
			value.append(base.charAt(nextInt));
		}
		return value.toString();
	}
	
	public static String randomNumber(int length){
		StringBuffer value = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int nextInt = random.nextInt(10);
			value.append(nextInt);
		}
		return value.toString();
	} 
}
