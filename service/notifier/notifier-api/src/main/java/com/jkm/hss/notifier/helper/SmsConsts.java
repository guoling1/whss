package com.jkm.hss.notifier.helper;

/**
 * Created by huangwei on 6/10/15.
 */
public final class SmsConsts {

    /**
     * 相同类型的验证码发送间隔(秒)
     */
    public static final int VERIFY_SEND_INTERVAL = 60;
    /**
     * 验证码有效时间(分)
     */
    public static final int VERIFY_VALID_PERIOD = 5;
    /**
     * 找回密码的验证码有效时间(分)
     */
    public static final int VERIFY_VALID_PERIOD_PASSWORD = 5 * 60;
    /**
     * 验证码重试次数
     */
    public static final int MAX_VERIFY_RETRY_COUNT = 3;


    private SmsConsts() {
    }

}
