package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by zhangbin on 2017/3/8.
 */
public enum EnumType {

    MAINTAIN (1, "维护"),

    NOTICE (2, "通知");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
