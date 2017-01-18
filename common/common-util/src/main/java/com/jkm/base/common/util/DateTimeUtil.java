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

    /**
     * 获取T1的结算时间
     *
     * @return
     */
    public static Date getSettleDate() {
        final DateTime now = DateTime.now();
        if (now.getDayOfWeek() >= 1 && now.getDayOfWeek() <= 4) {
            return now.plusDays(1).toDate();
        } else if (now.getDayOfWeek() == 5) {
            return now.plusDays(3).toDate();
        } else if (now.getDayOfWeek() == 6) {
            return now.plusDays(2).toDate();
        } else if (now.getDayOfWeek() == 7) {
            return now.plusDays(1).toDate();
        }
        throw new RuntimeException("can not be here");
    }
}
