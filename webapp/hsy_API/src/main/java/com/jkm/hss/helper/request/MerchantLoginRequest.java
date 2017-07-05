package com.jkm.hss.helper.request;

import lombok.Data;


@Data
public class MerchantLoginRequest {
    /**
     *
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
     * openid
     */
    private String openId;
}
