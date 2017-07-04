package com.jkm.base.common.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

/**
 * Created on 16/7/21.
 *
 * @author hutao
 * @version 1.0
 */
public class AESUtilTest {
    @Test
    public void should_success_use_aes_util() throws Exception {
        final String key = "dXl4tPw7aioiX7DWyJ7GBVa25ygWNva5";
        final String encrypt = AESUtil.encrypt("18501363079", key);
        final String decrypt = AESUtil.decrypt(encrypt, key);
        System.out.println(decrypt);
        System.out.println(AESUtil.decrypt("FBeHwEWj29cHq0O0B-fnb1", "JyfamXwoAs8OWUMEiFXGKLZNhz3EPNU1"));
    }

    @Test
    public void name() throws Exception {
        final String key = "JyfamXwoAs8OWUMEiFXGKLZNhz3EPNU1";
        for (int i = 0; i < 112000; i++) {
            final String str = RandomStringUtils.randomNumeric(11);
            final String encrypt = AESUtil.encrypt(str, key);
            final String decrypt = AESUtil.decrypt(encrypt, key);
            System.out.println(decrypt);
        }

    }
}