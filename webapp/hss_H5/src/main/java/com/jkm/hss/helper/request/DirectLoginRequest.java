package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/4.
 */
@Data
public class DirectLoginRequest {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 验证码
     */
    private String code;
}
