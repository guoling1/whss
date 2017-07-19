package com.jkm.hss.bill.helper.util;

import com.jkm.hsy.user.Enum.EnumPolicyType;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yulong.zhang on 2017/7/4.
 */
@Slf4j
@UtilityClass
public class VerifyAuthCodeUtil {

    public static final String WECHAT_PATTERN = "^1[0-5][0-9]{16}$";
    public static final String ALIPAY_PATTERN = "^(2[5-9]|30)[0-9]{14,22}$";

    /**
     * 校验授权码
     *
     * @param authCode
     * @return
     */
    public EnumPolicyType verify (final String authCode) {
        final Pattern wechatPattern = Pattern.compile(WECHAT_PATTERN);
        final Matcher wechatMatcher = wechatPattern.matcher(authCode);
        if(wechatMatcher.find()) {
            return EnumPolicyType.WECHAT;
        }
        final Pattern alipayPattern = Pattern.compile(ALIPAY_PATTERN);
        final Matcher alipayMatcher = alipayPattern.matcher(authCode);
        if(alipayMatcher.find()) {
            return EnumPolicyType.ALIPAY;
        }
        return null;
    }
}
