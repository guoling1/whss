package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-30.
 */
public enum EnumProductStatus {

    INIT(1, "初始化"),
    USEING(2, "产品使用中"),
    DELETE(3, "产品删除"),
    UNUSEING(4, "产品未使用");

    @Getter
    private int id;
    @Getter
    private String name;

    EnumProductStatus(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
