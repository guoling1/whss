package com.jkm.base.common.util;

import com.google.common.base.Throwables;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by huangwei on 7/27/15.
 */
public final class RC4 {

    private RC4() {
    }

    /**
     * 加密
     *
     * @param plaintext
     * @param pwd
     * @return
     */
    public static String encrypt(final String plaintext, final String pwd) {
        if (StringUtils.isBlank(plaintext)) {
            return plaintext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("RC4");
            final SecretKeySpec key = new SecretKeySpec(pwd.getBytes("UTF-8"), "RC4");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            final byte[] cdata = cipher.update(plaintext.getBytes("UTF-8"));
            return Base64.encodeBase64String(cdata);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    /**
     * 解密
     *
     * @param ciphertext
     * @param pwd
     * @return
     */
    public static String decrypt(final String ciphertext, final String pwd) {
        if (StringUtils.isBlank(ciphertext)) {
            return ciphertext;
        }
        try {
            final Cipher cipher = Cipher.getInstance("RC4");
            final SecretKeySpec key = new SecretKeySpec(pwd.getBytes("UTF-8"), "RC4");
            cipher.init(Cipher.DECRYPT_MODE, key);
            final byte[] ddata = cipher.update(Base64.decodeBase64(ciphertext));
            return new String(ddata, "UTF-8");
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

}
