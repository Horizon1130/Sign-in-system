package com.myway.platform.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public Date getBeginTime(String beginTime) {
		beginTime += " 00:00:00";
		try {
			Date time = dateFormat.parse(beginTime);
			return time;
		} catch (Exception e) {
		}
		return null;
	}

	public Date getEndTime(String endTime) {
		endTime += " 23:59:59";
		try {
			Date time = dateFormat.parse(endTime);
			return time;
		} catch (Exception e) {
		}
		return null;
	}
	
	public String getConstellation(String birthday) {
		String star = "";
		Date birthdayDate = getBeginTime(birthday);
		Calendar calendar_birthday = Calendar.getInstance();
		calendar_birthday.setTime(birthdayDate);
		int month = calendar_birthday.get(Calendar.MONTH) + 1;
		int day = calendar_birthday.get(Calendar.DATE);
		if (month == 1 && day >= 20 || month == 2 && day <= 18) {
			star = "水瓶座";
		}
		if (month == 2 && day >= 19 || month == 3 && day <= 20) {
			star = "双鱼座";
		}
		if (month == 3 && day >= 21 || month == 4 && day <= 19) {
			star = "白羊座";
		}
		if (month == 4 && day >= 20 || month == 5 && day <= 20) {
			star = "金牛座";
		}
		if (month == 5 && day >= 21 || month == 6 && day <= 21) {
			star = "双子座";
		}
		if (month == 6 && day >= 22 || month == 7 && day <= 22) {
			star = "巨蟹座";
		}
		if (month == 7 && day >= 23 || month == 8 && day <= 22) {
			star = "狮子座";
		}
		if (month == 8 && day >= 23 || month == 9 && day <= 22) {
			star = "处女座";
		}
		if (month == 9 && day >= 23 || month == 10 && day <= 23) {
			star = "天秤座";
		}
		if (month == 10 && day >= 24 || month == 11 && day <= 22) {
			star = "天蝎座";
		}
		if (month == 11 && day >= 23 || month == 12 && day <= 21) {
			star = "射手座";
		}
		if (month == 12 && day >= 22 || month == 1 && day <= 19) {
			star = "摩羯座";
		}
		return star;
	}
	
	public int getAgeByBirthday(String birthday) {
		int age = 0;
		Date nowDate = new Date();
		Date birthdayDate = getBeginTime(birthday);

		Calendar calendar_birthday = Calendar.getInstance();
		calendar_birthday.setTime(birthdayDate);
		int birth_month = calendar_birthday.get(Calendar.MONTH) + 1;
		int birth_year = calendar_birthday.get(Calendar.YEAR);

		Calendar calendar_now = Calendar.getInstance();
		calendar_now.setTime(nowDate);
		int now_month = calendar_now.get(Calendar.MONTH) + 1;
		int now_year = calendar_now.get(Calendar.YEAR);
		age = now_year - birth_year;
		if (now_month >= birth_month) {
			age++;
		}
		return age;
	}

}
