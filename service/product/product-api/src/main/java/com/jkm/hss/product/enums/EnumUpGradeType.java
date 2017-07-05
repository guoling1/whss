package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public enum  EnumUpGradeType {
    COMMON(0,"普通"),
    CLERK(1, "店员"),
    SHOPOWNER(2, "店长"),
    BOSS(3, "老板");

    @Getter
    private int id;
    @Getter
    private String name;

    EnumUpGradeType(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
