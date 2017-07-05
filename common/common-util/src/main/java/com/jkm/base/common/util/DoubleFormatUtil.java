package com.jkm.base.common.util;

import java.text.DecimalFormat;

/**
 * Created by hutao on 15/8/25.
 * 下午4:41
 */
public final class DoubleFormatUtil {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private DoubleFormatUtil() {
    }

    /**
     * double -> string like（1.25）
     * 2位精度
     *
     * @param amount
     * @return
     */
    public static String format(final double amount) {
        return DECIMAL_FORMAT.format(amount);
    }
}
