package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 16/1/27.
 * 下午2:30
 */
public class LuhnUtilTest {
    @Test
    public void testCheckCardNo() throws Exception {
        assertThat(LuhnUtil.checkCardNo("6214830109241413"), is(true));
        assertThat(LuhnUtil.checkCardNo("621483013444559241413"), is(false));
        assertThat(LuhnUtil.checkCardNo("3225425353452"), is(false));
    }
}