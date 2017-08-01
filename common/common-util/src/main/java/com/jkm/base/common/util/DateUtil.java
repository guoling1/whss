package com.jkm.base.common.util;

import com.jkm.base.common.constant.TimeConstant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by hutao on 15/7/9.
 * 下午5:50
 */
@UtilityClass
public final class DateUtil {

    /**
     * 创建单位为小时的TimeAmount 用于对date数据进行加减操作
     *
     * @param amount 可以是负数
     * @return
     */
    public static TimeAmount createHourAmount(final int amount) {
        return new TimeAmount(Calendar.HOUR, amount);
    }

    /**
     * 创建单位为分钟的TimeAmount 用于对date数据进行加减操作
     *
     * @param amount 可以是负数
     * @return
     */
    public static TimeAmount createMinuteAmount(final int amount) {
        return new TimeAmount(Calendar.MINUTE, amount);
    }

    /**
     * 对Date进行加减
     *
     * @param date
     * @param timeAmount
     * @return
     */
    public static Date addDate(final Date date, final TimeAmount timeAmount) {
        final GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(timeAmount.getField(), timeAmount.getAmount());
        return gc.getTime();
    }

    /**
     * 计算日期之间的天数间隔
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long getDaySpace(final Date d1, final Date d2) {
        final long spaceTime = d2.getTime() - d1.getTime();
        return spaceTime / TimeConstant.DAY;
    }

    /**
     * 是否相等
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isEqual(final Date d1, final Date d2) {
        return new DateTime(d1).isEqual(new DateTime(d2));
    }

    /**
     * 是否大于
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isAfter(final Date d1, final Date d2) {
        return new DateTime(d1).isAfter(new DateTime(d2));
    }

    /**
     * 是否小于
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isBefore(final Date d1, final Date d2) {
        return new DateTime(d1).isBefore(new DateTime(d2));
    }

    /**
     * 时间之差
     *
     * @param d1
     * @param d2
     * @return
     */
    public static int dayBetween(final Date d1, final Date d2) {
        return Days.daysBetween(new DateTime(d1), new DateTime(d2)).getDays();
    }

    /**
     * Date数据加减数量单位
     */
    @Getter
    public static class TimeAmount {
        private final int field;
        private final int amount;

        private TimeAmount(final int field, final int amount) {
            this.field = field;
            this.amount = amount;
        }
    }

    /**
     * 判断时间是否在时间段内 *
     * @param date
     * 当前时间 yyyy-MM-dd HH:mm:ss
     * @param strDateBegin
     * 开始时间 00:00:00
     * @param strDateEnd
     * 结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(Date date, String strDateBegin,String strDateEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(date);
        // 截取当前时间时分秒
        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));
        // 截取开始时间时分秒
        int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(6, 8));
        // 截取结束时间时分秒
        int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(6, 8));
        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
        // 当前时间小时数在开始时间和结束时间小时数之间
        if (strDateH > strDateBeginH && strDateH < strDateEndH) {
            return true;
            // 当前时间小时数等于开始时间小时数，分钟数在开始和结束之间
        } else if (strDateH == strDateBeginH && strDateM >= strDateBeginM&& strDateM <= strDateEndM) {
            return true;
            // 当前时间小时数等于开始时间小时数，分钟数等于开始时间分钟数，秒数在开始和结束之间
        } else if (strDateH == strDateBeginH && strDateM == strDateBeginM&& strDateS >= strDateBeginS && strDateS <= strDateEndS) {
            return true;
        }
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数小等于结束时间分钟数
        else if (strDateH >= strDateBeginH && strDateH == strDateEndH&& strDateM < strDateEndM) {
            return true;
            // 当前时间小时数大等于开始时间小时数，等于结束时间小时数，分钟数等于结束时间分钟数，秒数小等于结束时间秒数
        } else if (strDateH >= strDateBeginH && strDateH == strDateEndH&& strDateM == strDateEndM && strDateS <= strDateEndS) {
            return true;
        } else {
            return false;
        }
        } else {
            return false;
        }
    }

    public static Date changeDate(Date originDate, int calendarField, int distance) {
        Calendar c = Calendar.getInstance(Locale.CHINA);
        c.setTime(originDate);
        c.add(calendarField, distance);
        return c.getTime();
    }

    /**
     * 格式化一个日期数据.
     * @param date 需要格式的时间类型
     * @param formatStr 自定义格式
     * @return string
     */
    public static String formatDate(Date date,String formatStr) {
        return new SimpleDateFormat(formatStr).format(date);
    }
}
