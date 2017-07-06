package com.jkm.base.common.util;

import com.jkm.base.common.constant.TimeConstant;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
}
