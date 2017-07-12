package com.jkm.hss.notifier.service;

import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by huangwei on 6/11/15.
 */
public interface SmsAuthService {
    /**
     * 校验验证码
     *
     * @param mobile
     * @param code
     * @param verificationCodeType
     * @return 1:成功    -1:验证不存在    -2:验证码无效    -3:验证码过期    -4:验证码错误
     */
    Pair<Integer, String> checkVerifyCode(String mobile, String code, EnumVerificationCodeType verificationCodeType);

    /**
     * 获取验证码
     *
     * @param mobile
     * @param verificationCodeType
     * @return -1:手机号错误  -2:超过每天发送数  -3:验证码发送太频繁    -4:获取验证码错误
     */
    Pair<Integer, String>  getVerifyCode(String mobile, EnumVerificationCodeType verificationCodeType);
}
