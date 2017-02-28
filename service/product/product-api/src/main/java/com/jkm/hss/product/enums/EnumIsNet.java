package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * 是否需要入网
 */
public enum EnumIsNet {

    NEED(1, "需入网"),
    UNNEED(2, "无需入网");

    @Getter
    private int id;
    @Getter
    private String name;

    EnumIsNet(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
