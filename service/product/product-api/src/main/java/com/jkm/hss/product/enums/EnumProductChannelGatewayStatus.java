package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-03-30.
 */
public enum EnumProductChannelGatewayStatus {

    INIT(1, "初始化"),
    USEING(2, "通道模板使用中"),
    DELETE(3, "通道模板删除"),
    UNUSEING(4, "通道未使用");

    @Getter
    private int id;
    @Getter
    private String name;

    EnumProductChannelGatewayStatus(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
