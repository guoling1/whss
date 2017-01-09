package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public enum  EnumIsUpGrade {
    NOTUPGRADE(1, "不显示升级"),
    UPGRADE(2, "显示升级");
    @Getter
    private int id;
    @Getter
    private String name;

    EnumIsUpGrade(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
