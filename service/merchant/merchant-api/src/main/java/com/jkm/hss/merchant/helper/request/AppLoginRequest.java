package com.jkm.hss.merchant.helper.request;

import lombok.Data;


@Data
public class AppLoginRequest {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 手机验证码
     */
    private String code;
    /**
     * 分公司标示
     */
    private String oemNo;
}
