package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/23.
 *
 * 登录状态
 */
public enum EnumLoginStatus {

    LOGOUT(0),
    LOGIN(1);

    @Getter
    private int id;
    EnumLoginStatus(int id) {
        this.id = id;
    }
}
