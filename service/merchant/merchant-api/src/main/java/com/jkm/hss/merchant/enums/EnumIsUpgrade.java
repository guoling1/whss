package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/10.
 */
public enum EnumIsUpgrade {
    CANUPGRADE(1, "能升级"),

    CANNOTUPGRADE(2, "不能升级");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumIsUpgrade(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
