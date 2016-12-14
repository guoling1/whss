package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by zhangbin on 2016/11/29.
 */
@Data
public class RequestSendVerifyCode {

    /**
     * 商户id
     */
    private long id;

    /**
     * 预留手机号
     */
    private String reserveMobile;

    /**
     * code
     */
    private String code;
}
