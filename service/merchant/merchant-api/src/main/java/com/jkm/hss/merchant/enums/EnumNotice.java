package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public enum EnumNotice {
    DELETED(0, "已删除"),

    PUBLISHED(1, "已发布");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumNotice(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
