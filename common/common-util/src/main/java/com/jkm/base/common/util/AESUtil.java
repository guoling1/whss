package com.jkm.base.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.codec.binary.Base64;

import java.util.Arrays;

/**
 * Created by hutao on 15/11/25.
 */
public class AESUtil {
    /**
     * 私有构造方法
     */
    private AESUtil() {

    }

    /**
     * 使用key的前16位作为iv加密
     * key长度统一为32位
     *
     * @param input 待加密字符串
     * @param key   32位加密key
     * @return base64 编码后的加密字符串
     */
    public static String encrypt(final String input, final String key) {
        final byte[] keyByte = key.getBytes();
        Preconditions.checkState(keyByte.length == 32, "aes encrypt key length is not 32");

        final byte[] inputByte = input.getBytes();
        final byte[] ivByte = Arrays.copyOfRange(keyByte, 0, 16);

        final byte[] encryptStr = AES.encrypt(inputByte, keyByte, ivByte);

        return Base64.encodeBase64URLSafeString(encryptStr);
    }

    /**
     * 解密
     *
     * @param input 待解密字符串
     * @param key   32位加密key
     * @return
     */
    public static String decrypt(final String input, final String key) {
        final byte[] keyByte = key.getBytes();
        Preconditions.checkState(keyByte.length == 32, "aes decrypt key length is not 32");

        final byte[] inputByte = Base64.decodeBase64(input);
        final byte[] ivByte = Arrays.copyOfRange(keyByte, 0, 16);

        return new String(AES.decrypt(inputByte, keyByte, ivByte));
    }

}
