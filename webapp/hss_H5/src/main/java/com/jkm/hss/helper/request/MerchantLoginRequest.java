package com.jkm.hss.helper.request;

import lombok.Data;


@Data
public class MerchantLoginRequest {
    /**
     *扫码注册的二维码
     */
    private String qrCode;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * code
     */
    private String code;

    /**
     * 邀请码（邀请码或二维码至少与一个不为空）
     */
    private String inviteCode;
}
