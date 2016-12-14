package com.jkm.base.common.util;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by weihuang on 14-5-17.
 */
public final class EmojiUtils {

    /**
     *
     */
    private EmojiUtils() {
    }

    /**
     * 通过对文本当中的UTF-8字符进行手动的解析达到识别并转换UTF-8中没有 的表情符号的目的。<br/>
     * <br/>
     * UTF-8的编码有如下特点：<br/>
     * 如果一个字符是1字节（即ascii码），则该字符的格式为: 0xxxxxxx<br/>
     * 如果一个字符是2字节，则该字符的编码格式为：110xxxxx 10xxxxxx<br/>
     * 如果一个字符是3字节，则该字符的编码格式为：1110xxxx 10xxxxxx 10xxxxxx<br/>
     * <br/>
     * 目前sql无法解析的UTF-8表情编码为四个字节，即格式为：<br/>
     * 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx<br/>
     * <br/>
     * 所以本方法过滤emoji的方式就是逐个读取utf-8字符，判断该字符的字节数<br/>
     * 若小于4字节则直接转换成string保留<br/>
     * 若为4字节则将其编码成为:<br/>
     * <br/>
     * \:xxxxxxxx (如\:f09f8f86)<br/>
     * <br/>
     * 的格式，其中x代表1位16进制字符<br/>
     * 长度大于4字节的暂未处理，有无必要待确定。<br/>
     * <br/>
     *
     * @param text
     * @return
     */
    public static String emojiFilter(final String text) {
        if (text == null) {
            return "";
        }
        final byte[] textBytes = text.getBytes(Charset.forName("UTF-8"));
        final StringBuilder sb = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < textBytes.length; n++) {
            if ((textBytes[n] & 0XFF) >= 0XFC && (textBytes[n] & 0XFF) < 0XFE && n < textBytes.length - 5) {
                stmp = new String(textBytes, n, 6, Charset.forName("UTF-8"));
                n = n + 5;
            } else if ((textBytes[n] & 0XFF) >= 0XF8 && (textBytes[n] & 0XFF) < 0XFC && n < textBytes.length - 4) {
                stmp = new String(textBytes, n, 5, Charset.forName("UTF-8"));
                n = n + 4;
            } else if ((textBytes[n] & 0XFF) >= 0XF0 && (textBytes[n] & 0XFF) < 0XF8 && n < textBytes.length - 3) {
                stmp = convertToString(textBytes[n], textBytes[n + 1], textBytes[n + 2], textBytes[n + 3]);
                n = n + 3;
            } else if ((textBytes[n] & 0XFF) >= 0XE0 && (textBytes[n] & 0XFF) < 0XF0 && n < textBytes.length - 2) {
                stmp = new String(textBytes, n, 3, Charset.forName("UTF-8"));
                n = n + 2;
            } else if ((textBytes[n] & 0XFF) >= 0XC0 && (textBytes[n] & 0XFF) < 0XE0 && n < textBytes.length - 1) {
                stmp = new String(textBytes, n, 2, Charset.forName("UTF-8"));
                n++;
            } else if ((textBytes[n] & 0XFF) < 0X80) {
                stmp = new String(textBytes, n, 1, Charset.forName("UTF-8"));
            } else {// 残缺字节时忽略
                continue;
            }
            sb.append(stmp);
        }
        return sb.toString();
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source
     * @return
     */
    public static String killEmoji(final String source) {
        if (null == source || "".equals(source)) {
            return source;
        }
        final StringBuilder sb = new StringBuilder();
        for (final char c : source.toCharArray()) {
            if (c == 0x0 || c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD) || (c >= 0x10000 && c <= 0x10FFFF)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 通过分析固定格式：<br/>
     * \:xxxxxxxx (如\:f09f8f86)<br/>
     * 将该内容转化为4字节的utf-8表情字符<br/>
     * <br/>
     * 利用正则表达式进行匹配后逐一处理并替换<br/>
     * <br/>
     *
     * @param encodeText 经过emojiFilter过滤的字符串
     * @return 还原后的字符串
     */
    public static String recoverToEmoji(final String encodeText) throws PatternSyntaxException {
        if (encodeText == null) {
            return "";
        }

        final String regEx = "(\\\\:)([0-9a-fA-F]{8})";
        final Pattern pattern = Pattern.compile(regEx);
        final Matcher matcher = pattern.matcher(encodeText);
        final StringBuffer sb = new StringBuffer();

        while (matcher.find()) {
            String temp = matcher.group(2);
            temp = new String(hexStringToBytes(temp), Charset.forName("UTF-8"));
            matcher.appendReplacement(sb, temp);
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static byte[] hexStringToBytes(final String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        final String _hexString = hexString.toUpperCase(Locale.ENGLISH);
        final int length = _hexString.length() / 2;
        final char[] hexChars = _hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            final int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    private static byte charToByte(final char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    private static String convertToString(final byte a, final byte b, final byte c, final byte d) {
        final StringBuilder sb = new StringBuilder("\\:");
        sb.append(Integer.toHexString(a & 0XFF)).append(Integer.toHexString(b & 0XFF))
                .append(Integer.toHexString(c & 0XFF)).append(Integer.toHexString(d & 0XFF));
        return sb.toString();
    }

}
