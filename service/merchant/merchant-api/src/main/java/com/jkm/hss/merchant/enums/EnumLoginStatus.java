package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 *
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
