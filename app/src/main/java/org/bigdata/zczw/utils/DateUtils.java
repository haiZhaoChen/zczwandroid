package org.bigdata.zczw.utils;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *  Created by bob on 2015/2/28.
 */
public class DateUtils {
	/**
	 * 字符串类型的时间戳转化为时间
	 * @param time
	 * @return
	 */
	public static String convertToDate(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String date = sdf.format(new Date(Long.parseLong(time)));
		return date;
	}
	public static String convertToOldDate(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
		String date = sdf.format(new Date(Long.parseLong(time)));
		return date;
	}
	public static String convertToDate2(String time){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new Date(Long.parseLong(time)));
		return date;
	}
	 /**
	  * 得到当前时间
	  * @param dateFormat 时间格式
	  * @return 转换后的时间格式
	  */
	 public static String getStringToday(String dateFormat) {
		  Date currentTime = new Date();
		  SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		  String dateString = formatter.format(currentTime);
		  return dateString;
	 }

	 /**
	  * 将字符串型日期转换成日期
	  * @param dateStr 字符串型日期
	  * @param dateFormat 日期格式
	  * @return
	  */
	 public static Date stringToDate(String dateStr, String dateFormat) {
		 SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		 try {
			return formatter.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	 }
	 
	 /**
	  * 日期转字符串
	  * @param date
	  * @param dateFormat
	  * @return
	  */
	 public static String dateToString(Date date, String dateFormat) {
		 SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
		 return formatter.format(date);
	 }

    /**
     * 两个时间点的间隔时长（分钟）
     * @param before 开始时间
     * @param after 结束时间
     * @return 两个时间点的间隔时长（分钟）
     */
    public static long compareMin(Date before, Date after) {
        if (before == null || after == null) {
            return 0l;
        }
        long dif = 0;
        if(after.getTime() >= before.getTime()) {
             dif = after.getTime() - before.getTime();
        }else if(after.getTime() < before.getTime()){
            dif = after.getTime() + 86400000 - before.getTime();
        }
        dif = Math.abs(dif);
        return dif  / 60000;
    }

    /**
     * 获取指定时间间隔分钟后的时间
     * @param date 指定的时间
     * @param min 间隔分钟数
     * @return 间隔分钟数后的时间
     */
	public static Date addMinutes(Date date, int min) {
		if (date == null) {
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);
		return calendar.getTime();
	}

    /**
     * 根据时间返回指定术语，自娱自乐，可自行调整
     * @param hourday 小时
     * @return
     */
    public static String showTimeView(int hourday) {
        if(hourday >= 22 && hourday <= 24){
            return "晚上";
        }else if(hourday >= 0 && hourday <= 6 ){
            return  "凌晨";
        }else if(hourday > 6 && hourday <= 12){
            return "上午";
        }else if(hourday >12 && hourday < 22){
            return "下午";
        }
        return null;
    }
    
    
    /***
	 * MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}
	/**
	 * 通过年份和月份 得到当月的日子
	 *
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getMonthDays(int year, int month) {
		month++;
		switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return 31;
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
			case 2:
				if (((year % 4 == 0) && (year % 100 != 0)) || (year % 400 == 0)){
					return 29;
				}else{
					return 28;
				}
			default:
				return  -1;
		}
	}
}
