package com.jkm.base.common.util;

import com.google.common.base.Throwables;
import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

/**
 * Created by hutao on 16/1/21.
 * 下午8:01
 */
@UtilityClass
public class TripleDESUtil {
    private static final String TRIPLE_DES_ALGORITHM_KEY = "DESede/ECB/PKCS7Padding";
    private static final String TRIPLE_DES_ALGORITHM_NAME = "DESede";

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    /**
     * DESede/ECB/PKCS7Padding 加密
     *
     * @param input
     * @param key   16位
     * @param iv    16位
     * @return
     */
    public static byte[] encrypt(final byte[] input, final byte[] key, final byte[] iv) {
        final SecretKeySpec keySpec = new SecretKeySpec(key, TRIPLE_DES_ALGORITHM_NAME);
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(TRIPLE_DES_ALGORITHM_KEY);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * DESede/ECB/PKCS7Padding 解密
     *
     * @param input
     * @param key   16位
     * @param iv    16位
     * @return
     */
    public static byte[] decrypt(final byte[] input, final byte[] key, final byte[] iv) {
        final SecretKeySpec keySpec = new SecretKeySpec(key, TRIPLE_DES_ALGORITHM_NAME);
        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(TRIPLE_DES_ALGORITHM_KEY);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * DESede/ECB/PKCS7Padding 加密
     *
     * @param input
     * @param key   16位
     * @return
     */
    public static byte[] encrypt(final byte[] input, final byte[] key) {
        final SecretKeySpec keySpec = new SecretKeySpec(key, TRIPLE_DES_ALGORITHM_NAME);
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(TRIPLE_DES_ALGORITHM_KEY, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * DESede/ECB/PKCS7Padding 解密
     *
     * @param input
     * @param key   16位
     * @return
     */
    public static byte[] decrypt(final byte[] input, final byte[] key) {
        final SecretKeySpec keySpec = new SecretKeySpec(key, TRIPLE_DES_ALGORITHM_NAME);
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(TRIPLE_DES_ALGORITHM_KEY, "BC");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(input);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }
}
