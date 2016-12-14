package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 15/11/12.
 * 下午12:06
 */
public class StringFormatterTest {
    @Test
    public void testFormat() throws Exception {
        assertThat(StringFormatter.format("%saaaddd%sdfadsf", 1, 2), is("1aaaddd2dfadsf"));
    }
}