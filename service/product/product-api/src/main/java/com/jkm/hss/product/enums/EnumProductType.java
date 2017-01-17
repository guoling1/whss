package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/9.
 */
public enum EnumProductType {
    HSS("hss", "好收收"),
    HSY("hsy", "好收银");


    @Getter
    private String id;
    @Getter
    private String name;

    EnumProductType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
