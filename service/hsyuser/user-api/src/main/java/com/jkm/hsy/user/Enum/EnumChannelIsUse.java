package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/6/12.
 */
public enum EnumChannelIsUse {
    USE(1, "使用中"),

    NOTUSE(2, "未使用");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumChannelIsUse(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
