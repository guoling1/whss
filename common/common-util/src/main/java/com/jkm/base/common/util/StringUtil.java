package com.jkm.base.common.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by huangwei on 5/19/16.
 */
@UtilityClass
public class StringUtil {

    /**
     * 得到一个字符串的长度,一个汉字或日韩文长度为2,英文字符长度为1
     *
     * @param str
     * @return
     */
    public static int length(final String str) {
        if (StringUtils.isBlank(str)) {
            return 0;
        }

        final String chinese = "[\u0391-\uFFE5]";

        int length = 0;
        for (Character c : str.toCharArray()) {
            if (c.toString().matches(chinese)) {
                length += 2;
            } else {
                length++;
            }
        }
        return length;
    }

}
