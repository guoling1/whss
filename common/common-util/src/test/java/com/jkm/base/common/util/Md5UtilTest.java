package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 16/1/22.
 * 下午5:08
 */
public class Md5UtilTest {
    @Test
    public void testMd5() throws Exception {
        assertThat(BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest("123456".getBytes())), is("e10adc3949ba59abbe56e057f20f883e"));
        assertThat(BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest("张三".getBytes())), is("615db57aa314529aaa0fbe95b3e95bd3"));
    }
}