package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/24.
 *
 * 后台登录 入参
 */
@Data
public class AdminUserLoginRequest {

    private String username;
    private String password;
}
