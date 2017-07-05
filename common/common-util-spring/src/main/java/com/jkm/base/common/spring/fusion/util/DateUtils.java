package com.jkm.base.common.spring.fusion.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理常用类
 * @author wujh
 *
 */
public class DateUtils {

	public static final String formate_string_yyyyMMdd = "yyyyMMdd";
	
	public static final String formate_string_yyyyMMddhhmmss = "yyyyMMddHHmmss";
	
	public static final String formate_string_hhmmss = "HHmmss";
	
	public static String getDateString(Date d, String format){
		DateFormat df = new SimpleDateFormat(format);
		return df.format(new Date());
	}
	
	public static Date converStringToDate(String dateStr, String format){
		DateFormat df = new SimpleDateFormat(format);
		try {
			return df.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
}
