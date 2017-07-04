package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/4/11.
 */
public enum EnumCleanType {
    CVV(1, "清空cvv"),

    EXPIRYTIME(2, "清空有效期"),

    CVVANDEXPIRYTIME(3,"清空两者");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumCleanType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
