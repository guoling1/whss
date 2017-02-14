package com.jkm.base.common.util;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangwei on 3/30/15.
 */
public final class ValidateUtils {
    final static int[] PARITYBIT = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    final static int[] POWER_LIST = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final Map<Integer, String> ZONE_NUM = new HashMap<>();

    static {
        ZONE_NUM.put(11, "北京");
        ZONE_NUM.put(12, "天津");
        ZONE_NUM.put(13, "河北");
        ZONE_NUM.put(14, "山西");
        ZONE_NUM.put(15, "内蒙古");
        ZONE_NUM.put(21, "辽宁");
        ZONE_NUM.put(22, "吉林");
        ZONE_NUM.put(23, "黑龙江");
        ZONE_NUM.put(31, "上海");
        ZONE_NUM.put(32, "江苏");
        ZONE_NUM.put(33, "浙江");
        ZONE_NUM.put(34, "安徽");
        ZONE_NUM.put(35, "福建");
        ZONE_NUM.put(36, "江西");
        ZONE_NUM.put(37, "山东");
        ZONE_NUM.put(41, "河南");
        ZONE_NUM.put(42, "湖北");
        ZONE_NUM.put(43, "湖南");
        ZONE_NUM.put(44, "广东");
        ZONE_NUM.put(45, "广西");
        ZONE_NUM.put(46, "海南");
        ZONE_NUM.put(50, "重庆");
        ZONE_NUM.put(51, "四川");
        ZONE_NUM.put(52, "贵州");
        ZONE_NUM.put(53, "云南");
        ZONE_NUM.put(54, "西藏");
        ZONE_NUM.put(61, "陕西");
        ZONE_NUM.put(62, "甘肃");
        ZONE_NUM.put(63, "青海");
        ZONE_NUM.put(64, "宁夏");
        ZONE_NUM.put(65, "新疆");
        ZONE_NUM.put(71, "台湾");
        ZONE_NUM.put(81, "香港");
        ZONE_NUM.put(82, "澳门");
        ZONE_NUM.put(91, "外国");
    }

    private ValidateUtils() {
    }

    /**
     * 身份证验证
     *
     * @param cardId
     * @return
     */
    public static boolean isIDCard(final String cardId) {
        if (cardId == null || (cardId.length() != 18)) {
            return false;
        }

        //校验区位码
        if (!ZONE_NUM.containsKey(Integer.valueOf(cardId.substring(0, 2)))) {
            return false;
        }

        //校验年份
        final String year = cardId.substring(6, 10);
        final int iyear = Integer.parseInt(year);
        if (iyear < 1900 || iyear > new DateTime().getYear()) {
            return false;//1900年的PASS，超过今年的PASS
        }

        //校验月份
        final String month = cardId.substring(10, 12);
        final int imonth = Integer.parseInt(month);
        if (imonth < 1 || imonth > 12) {
            return false;
        }

        //校验天数
        final String day = cardId.substring(12, 14);
        final int iday = Integer.parseInt(day);
        if (iday < 1 || iday > new DateTime().monthOfYear().setCopy(imonth).dayOfMonth().getMaximumValue()) {
            return false;
        }

        final char[] cs = cardId.toUpperCase(Locale.ENGLISH).toCharArray();
        //校验位数
        int power = 0;
        for (int i = 0; i < cs.length; i++) {
            if (i == cs.length - 1 && cs[i] == 'X') {
                break;//最后一位可以 是X或x
            }
            if (cs[i] < '0' || cs[i] > '9') {
                return false;
            }
            if (i < cs.length - 1) {
                power += (cs[i] - '0') * POWER_LIST[i];
            }
        }

        //校验"校验码"
        return cs[cs.length - 1] == PARITYBIT[power % 11];
    }

    /**
     * 判读是否是中文名
     *
     * @param name
     * @return
     */
    public static boolean isChineseName(final String name) {
        final Pattern pattern = Pattern.compile("^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]){2,20}$");
        final Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * 判断是否是英文名
     *
     * @param name
     * @return
     */
    public static boolean isEnglishName(final String name) {
        final Pattern pattern = Pattern.compile("^[a-z,A-Z]{3,20}$");
        final Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * 校验手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(final String mobile) {
        final Pattern pattern = Pattern.compile("^1\\d{10}$");
        final Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    /**
     * 判断是不是url
     *
     * @param url
     * @return
     */
    public static boolean isUrl(final String url) {
        final Pattern pattern = Pattern.compile("[a-zA-z]+://[^\\s]*");
        final Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

    /**
     * 6位数字验证码校验
     *
     * @param verifyCode
     * @return
     */
    public static boolean verifyCodeCheck(final String verifyCode) {
        final String _code = StringUtils.trimToEmpty(verifyCode);
        return _code.length() == 6 && StringUtils.isNumeric(_code);
    }
    /**
     * 校验邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(final String email) {
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);
    }
}
