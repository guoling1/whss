package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/6/9.
 */
public enum EnumIsOpen {
    OPEN(1, "开通"),

    UNOPEN(2, "不开通");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumIsOpen(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
