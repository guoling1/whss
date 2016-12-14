package com.jkm.base.common.util;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by hutao on 16/1/22.
 * 上午10:46
 */
public class TripleDESUtilTest {


    @Test
    public void testEncrypt() throws Exception {
        final String key = "JUNNET_123456_123456_COM";
        final String str = "张三";
        final byte[] encryptBytes = TripleDESUtil.encrypt(str.getBytes("gbk"), key.getBytes());
        final String encryptStr = BytesHexConverterUtil.bytesToHexStr(encryptBytes);
        assertThat(encryptStr, is("9d639d587131dde5"));
        assertThat(new String(TripleDESUtil.decrypt(encryptBytes, key.getBytes()), "GBK"), is(str));
    }
}