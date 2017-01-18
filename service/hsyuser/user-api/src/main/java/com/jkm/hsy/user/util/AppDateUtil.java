package com.jkm.hsy.user.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AppDateUtil {
    public static final String TIME_FORMAT_SHORT = "yyyyMMddHHmmss";

    public static final String TIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";

    public static final String TIME_FORMAT_ENGLISH = "MM/dd/yyyy HH:mm:ss";

    public static final String DATE_FORMAT_SHORT = "yyyyMMdd";

    public static final String DATE_FORMAT_NORMAL = "yyyy-MM-dd";

    public static final String DATE_FORMAT_ENGLISH = "MM/dd/yyyy";

    public static final String DATE_FORMAT_MINUTE = "yyyyMMddHHmm";

    /**
     * 格式化一个日期数据.
     * @param date 需要格式的时间类型
     * @param formatStr 自定义格式
     * @return string
     */
    public static String formatDate(Date date,String formatStr) {
        return new SimpleDateFormat(formatStr).format(date);
    }

    /**
     * 按自定义解析字符串为日期.
     * @param dateStr 日期字符串
     * @param formatStr 自定义格式
     * @return date
     */
    public static Date parseDate(String dateStr,String formatStr) {
        try {
            return new SimpleDateFormat(formatStr).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按自定义解析字符串为日期.
     * @param dateStr 日期字符串
     * @return date
     */
    public static Date parseDate(String dateStr) {
        DateFormat fmt = null;
        if (dateStr.matches("\\d{14}")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_SHORT);
        } else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_NORMAL);
        } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{1,2}:\\d{1,2}:\\d{1,2}")) {
            fmt = new SimpleDateFormat(TIME_FORMAT_ENGLISH);
        }  else if (dateStr.matches("\\d{8}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_SHORT);
        } else if (dateStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_NORMAL);
        } else if (dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_ENGLISH);
        } else if (dateStr.matches("\\d{4}\\d{1,2}\\d{1,2}\\d{1,2}\\d{1,2}")) {
            fmt = new SimpleDateFormat(DATE_FORMAT_MINUTE);
        }
        try {
            return fmt.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 改变时间 譬如： changeDate(new Date(),Calendar.DATE, 2) 就是把当前时间加两天
     * @param originDate 原始时间
     * @param calendarField 改变类型(取值为Calendar的取值)
     * @param distance 长度
     * @return 改变后的时间
     */
    public static Date changeDate(Date originDate, int calendarField, int distance) {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTime(originDate);
        c.add(calendarField, distance);
        return c.getTime();
    }
}
