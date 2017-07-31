package com.jkm.hss.merchant.helper.request;

import lombok.Data;


@Data
public class AppRegisterRequest {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 手机验证码
     */
    private String code;

    /**
     * 邀请码（邀请码或二维码至少与一个不为空）
     */
    private String inviteCode;
}
