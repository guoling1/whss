package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public enum EnumIsReal {
    DIRECT(1, "不是真实好友"),

    INDIRECT(2, "是真实好友");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumIsReal(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
