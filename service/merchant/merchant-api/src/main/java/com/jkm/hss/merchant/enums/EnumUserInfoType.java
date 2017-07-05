package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/3.
 */
public enum EnumUserInfoType {
    HSS(0, "好收收"),

    HSY(1, "好收银");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumUserInfoType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
