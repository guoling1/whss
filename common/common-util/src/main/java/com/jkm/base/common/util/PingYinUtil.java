package com.jkm.base.common.util;

import lombok.experimental.UtilityClass;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.CharUtils;

/**
 * Created by hutao on 16/3/4.
 * 下午6:47
 */
@UtilityClass
public class PingYinUtil {
    /**
     * 获取拼音首字母
     *
     * @param c
     * @return
     */
    public static char getInitial(final char c) {
        if (CharUtils.isAsciiAlphanumeric(c)) {
            return c;
        }
        final String[] stringArray = PinyinHelper.toHanyuPinyinStringArray(c);
        return stringArray[0].charAt(0);
    }
}
