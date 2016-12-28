package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/23.
 *
 * 经销商 登录发送验证码请求入参
 */
@Data
public class DealerSendVerifyCodeRequest {

    /**
     * 手机号
     */
    private String mobile;
}
