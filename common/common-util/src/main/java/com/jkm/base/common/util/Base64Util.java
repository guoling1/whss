package com.jkm.base.common.util;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by hutao on 15/6/19.
 * 上午11:10
 */
public final class Base64Util {
    private Base64Util() {
    }

    /**
     * 获取Base64编码
     *
     * @param bytes 字节流
     * @return Base64编码
     */
    public static String encode(final byte[] bytes) {
        try {
            return new String(Base64.encodeBase64(bytes));
        } catch (Exception e) {
            throw new RuntimeException("获取Base64编码失败:" + e.getMessage(), e);
        }
    }

    /**
     * Base64解码
     *
     * @param str 字符串
     * @return 字节流
     */
    public static byte[] decode(final String str) {
        try {
            return Base64.decodeBase64(str);
        } catch (Exception e) {
            throw new RuntimeException("Base64解码失败:" + e.getMessage(), e);
        }
    }
}
