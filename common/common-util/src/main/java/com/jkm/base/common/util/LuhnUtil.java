package com.jkm.base.common.util;

import lombok.experimental.UtilityClass;

/**
 * Created by hutao on 16/1/27.
 * 下午2:29
 */
@UtilityClass
public class LuhnUtil {
    /**
     * 检查银行卡号是否合法
     *
     * @param cardNo
     * @return
     */
    public static boolean checkCardNo(final String cardNo) {
        int s1 = 0, s2 = 0;
        final String reverse = new StringBuffer(cardNo).reverse().toString();
        for (int i = 0; i < reverse.length(); ++i) {
            final int digit = Character.digit(reverse.charAt(i), 10);
            if ((i & 1) == 0) {
                s1 += digit;
            } else {
                s2 += digit >> 1;
                if (digit >= 5) {
                    s2 -= 9;
                }
            }
        }
        return (s1 + s2) % 10 == 0;
    }
}
