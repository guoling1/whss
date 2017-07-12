package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 16/3/4.
 * 下午6:47
 */
public class PingYinUtilTest {
    @Test
    public void should_success_get_initial() throws Exception {
        assertThat(PingYinUtil.getInitial('重'), is('z'));
        assertThat(PingYinUtil.getInitial('北'), is('b'));
        assertThat(PingYinUtil.getInitial('c'), is('c'));
    }
}