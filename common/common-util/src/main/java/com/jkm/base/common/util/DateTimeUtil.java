package com.jkm.base.common.util;

import lombok.experimental.UtilityClass;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by hutao on 15/12/14.
 * 下午8:30
 */
@UtilityClass
public class DateTimeUtil {
    /**
     * 昨日
     *
     * @return
     */
    public static DateTime yesterday() {
        return DateTime.now().minusDays(1).withTimeAtStartOfDay();
    }

    /**
     * 昨日
     *
     * @return
     */
    public static Date yesterdayDate() {
        return yesterday().toDate();
    }

    /**
     * 昨日
     *
     * @return
     */
    public static DateTime today() {
        return DateTime.now().withTimeAtStartOfDay();
    }

    /**
     * 昨日
     *
     * @return
     */
    public static Date todayDate() {
        return today().toDate();
    }
}
