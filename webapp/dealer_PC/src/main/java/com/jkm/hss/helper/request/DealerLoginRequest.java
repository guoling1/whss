package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Data
public class DealerLoginRequest {
    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     *
     */
    private String pwd;
}
