package com.jkm.base.common.util;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/7/9.
 * 下午6:58
 */
public class DateUtilTest {
    @Test
    public void should_success_add_date() throws Exception {
        Date date = new Date();
        assertThat(DateUtil.addDate(date, DateUtil.createHourAmount(10))
                .getTime() - date.getTime(), is(36000000l));
        assertThat(DateUtil.addDate(date, DateUtil.createHourAmount(-10))
                .getTime() - date.getTime(), is(-36000000L));
        assertThat(DateUtil.addDate(date, DateUtil.createMinuteAmount(10))
                .getTime() - date.getTime(), is(600000l));
        assertThat(DateUtil.addDate(date, DateUtil.createMinuteAmount(-10))
                .getTime() - date.getTime(), is(-600000L));
    }

    @Test
    public void should_success_get_two_date_day_space() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertThat(DateUtil.getDaySpace(sdf.parse("2008-08-08 12:10:12"),
                sdf.parse("2008-08-28 12:10:12")), is(20l));
        assertThat(DateUtil.getDaySpace(sdf.parse("2008-08-08 22:10:12"),
                sdf.parse("2008-08-28 12:10:12")), is(19l));
        assertThat(DateUtil.getDaySpace(sdf.parse("2008-02-08 12:10:12"),
                sdf.parse("2008-03-28 12:10:12")), is(49l));
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        assertThat(DateUtil.getDaySpace(sdf1.parse("2008-02-01"),
                sdf1.parse("2008-02-28")), is(27l));
    }
}