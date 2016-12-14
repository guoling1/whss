package com.jkm.base.common.util;

import com.google.common.base.Throwables;
import lombok.experimental.UtilityClass;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by hutao on 15/12/6.
 * 下午2:05
 */
@UtilityClass
public class Md5Util {
    /**
     * 加密md5
     *
     * @param bytes
     * @return
     */
    public static byte[] md5Digest(final byte[] bytes) {
        try {
            final MessageDigest digest = getMd5MessageDigest();
            return digest.digest(bytes);
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }

    private static MessageDigest getMd5MessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }
}
