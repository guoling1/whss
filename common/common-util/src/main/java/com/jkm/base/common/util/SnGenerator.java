package com.jkm.base.common.util;

import com.google.common.base.Preconditions;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Date;

/**
 * Created by hutao on 15/6/12.
 */
public final class SnGenerator {
    private SnGenerator() {
    }

    /**
     * 生成订单号
     * JKM + 时间戳＋5位随机数
     *
     * @return
     */
    public static String generate() {
        return "JKM" + DateFormatUtil.format(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(5);
    }

    /**
     * 生成length位订单号
     * "JKM" + 时间戳＋＊位随机数
     *
     * @return
     */
    public static String generate(final int length) {
        Preconditions.checkArgument(length > 17);
        return "JKM" + DateFormatUtil.format(new Date(), "yyyyMMddHHmmssSSS") +
                RandomStringUtils.randomNumeric(length - 17);
    }

    /**
     * 生成length位订单号
     * 前缀＋时间戳＋*位随机数
     *
     * @return
     */
    public static String generate(final String prefix, final int length) {
        final int minLength = 17 + prefix.length();
        Preconditions.checkArgument(length > minLength);
        return prefix + DateFormatUtil.format(new Date(), "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(length - minLength);
    }
}
