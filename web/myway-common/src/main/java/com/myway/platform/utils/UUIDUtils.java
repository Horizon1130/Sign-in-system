package com.myway.platform.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String getEncryUUID(){
		String replace = UUID.randomUUID().toString().replace("-", "");
		return MD5Util.MD5Encode(replace, "utf-8");
	}

	public static void main(String[] args) {

		System.out.println(RandomUtil.randomString(24));
		System.out.println(RandomUtil.randomString(30));
		System.out.println("LTAI4G7QYyy3ppRcTrHE87Ee".length());

		System.out.println("LTAI4G7QYyy3ppRcTrHE87Ee".length());
		System.out.println("UzSfLdYGuvubu0OdiBxaB8ev0Tpydy".length());
//		System.out.println("格式前的UUID ： " + UUID.randomUUID().toString());
//		System.out.println("格式化后的UUID ：" + getUUID());
//		System.out.println("加密的UUID ：" + getEncryUUID());
	}
}
