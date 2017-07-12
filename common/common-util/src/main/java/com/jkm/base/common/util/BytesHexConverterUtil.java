package com.jkm.base.common.util;

/**
 * Created by hutao on 15/12/1.
 * 下午8:36
 */
public final class BytesHexConverterUtil {
    private static final char[] BCD_LOOKUP = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    private BytesHexConverterUtil() {
    }

    /**
     * bytes to hex str
     *
     * @param bytes
     * @return
     */
    public static String bytesToHexStr(byte[] bytes) {
        final StringBuilder s = new StringBuilder(bytes.length * 2);

        for (final byte b : bytes) {
            s.append(BCD_LOOKUP[(b >>> 4) & 0x0f]);
            s.append(BCD_LOOKUP[b & 0x0f]);
        }

        return s.toString();
    }

    /**
     * hex str to bytes
     *
     * @param s
     * @return
     */
    public static byte[] hexStrToBytes(final String s) {
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2),
                    16);
        }

        return bytes;
    }
}
