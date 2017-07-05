package com.jkm.base.common.xml;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.junit.Test;

/**
 * Created by hutao on 15/12/2.
 * 下午7:28
 */
public class XMLTest {
    @Test
    public void name() throws Exception {
        final DateTime today = new DateTime().withTimeAtStartOfDay();
        System.out.println(Days.daysBetween(today, today.plusDays(1)).getDays());

    }
}