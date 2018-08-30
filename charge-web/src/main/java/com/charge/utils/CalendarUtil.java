package com.charge.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {

	/**
	 * 获取上个月指定格式字符串日期
	 * @param format
	 * @return
	 */
	public static String getLastMonthDate(String format){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		date = calendar.getTime();
		String lastMonthStr = new SimpleDateFormat(format).format(date);
		return lastMonthStr;
	}
	
	/**
	 * 获取当月最后一天
	 * @return
	 */
	public static int getSameMonthLastDay(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1,1);
		calendar.add(Calendar.DATE, -1);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return day;
	}

	public static String getFirstMonthDay(String format){
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 0);
		date = calendar.getTime();
		String lastMonthStr = new SimpleDateFormat(format).format(date);
		return lastMonthStr;
	}


	/**
	 * 获取上个月最后一天日期
	 * @return
	 */
	public static String getPreMonthLastDayDate(String format){
		Calendar calendar = Calendar.getInstance();
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),1);
		calendar.add(Calendar.DATE, -1);
		return new SimpleDateFormat(format).format(calendar.getTime());
	}
	
	
	/**
	 * 获取格式化日期
	 * @param format
	 * @return
	 */
	public static String getFormatDate(String format){
		return new SimpleDateFormat(format).format(new Date());
	}
}

