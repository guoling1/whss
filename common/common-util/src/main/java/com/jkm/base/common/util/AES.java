package com.jkm.base.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by huangwei on 11/9/15.
 * AES 加解密
 */
@Slf4j
public final class AES {
    private AES() {
    }

    /**
     * AES/CBC/PKCS5Padding 加密
     *
     * @param input
     * @param key   16位
     * @param iv    16位
     * @return
     */
    public static byte[] encrypt(final byte[] input, final byte[] key, final byte[] iv) {
        final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        final Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (final NoSuchAlgorithmException e) {
            log.error("", e);
        } catch (final NoSuchPaddingException e) {
            log.error("", e);
        } catch (final BadPaddingException e) {
            log.error("", e);
        } catch (final InvalidKeyException e) {
            log.error("", e);
        } catch (final IllegalBlockSizeException e) {
            log.error("", e);
        } catch (final InvalidAlgorithmParameterException e) {
            log.error("", e);
        }

        return null;
    }

    /**
     * AES/CBC/PKCS5Padding 解密
     *
     * @param input
     * @param key   16位
     * @param iv    16位
     * @return
     */
    public static byte[] decrypt(final byte[] input, final byte[] key, final byte[] iv) {
        final SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        final IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        final Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
            return cipher.doFinal(input);
        } catch (final NoSuchAlgorithmException e) {
            log.error("", e);
        } catch (final NoSuchPaddingException e) {
            log.error("", e);
        } catch (final BadPaddingException e) {
            log.error("", e);
        } catch (final InvalidKeyException e) {
            log.error("", e);
        } catch (final IllegalBlockSizeException e) {
            log.error("", e);
        } catch (final InvalidAlgorithmParameterException e) {
            log.error("", e);
        }

        return null;
    }

}
