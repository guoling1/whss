package com.jkm.base.common.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yulong.zhang on 2016/11/15.
 */
public class DateFormatUtil {
    public static final String yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyy_MM_dd_hh_mm_ss = "yyyy-MM-dd hh:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";


    /**
     * DateFormat maps
     */
    private static final ImmutableMap<String, ThreadLocal<DateFormat>> DATE_FORMAT_IMMUTABLE_MAP = new ImmutableMap.Builder<String, ThreadLocal<DateFormat>>()
            .put(yyyy_MM_dd_HH_mm_ss, new ThreadLocal<DateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(yyyy_MM_dd_HH_mm_ss, Locale.CHINA);
                }
            })
            .put(yyyy_MM_dd_hh_mm_ss, new ThreadLocal<DateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(yyyy_MM_dd_hh_mm_ss, Locale.CHINA);
                }
            })
            .put(yyyyMMddHHmmss, new ThreadLocal<DateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(yyyyMMddHHmmss, Locale.CHINA);
                }
            })
            .put(yyyyMMdd, new ThreadLocal<DateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(yyyyMMdd, Locale.CHINA);
                }
            })
            .put(yyyy_MM_dd, new ThreadLocal<DateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(yyyy_MM_dd, Locale.CHINA);
                }
            })
            .build();

    private DateFormatUtil() {
    }

    /**
     * format date to string
     *
     * @param date
     * @param format
     * @return
     */
    public static String format(final Date date, final String format) {
        if (date == null) {
            return "";
        }
        Preconditions.checkArgument(DATE_FORMAT_IMMUTABLE_MAP.containsKey(format), format);
        return DATE_FORMAT_IMMUTABLE_MAP.get(format).get().format(date);
    }

    /**
     * format date to string
     *
     * @param date
     * @return
     */
    public static String format(final Date date) {
        return format(date, yyyy_MM_dd_HH_mm_ss);
    }

    /**
     * date str -> date
     *
     * @param date
     * @param format
     * @return
     */
    public static Date parse(final String date, final String format) {
        Preconditions.checkArgument(DATE_FORMAT_IMMUTABLE_MAP.containsKey(format), format);
        try {
            return DATE_FORMAT_IMMUTABLE_MAP.get(format).get().parse(date);
        } catch (final ParseException e) {
            throw Throwables.propagate(e);
        }
    }
}
