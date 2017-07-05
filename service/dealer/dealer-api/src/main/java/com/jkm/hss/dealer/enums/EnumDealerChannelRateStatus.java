package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-30.
 */
public enum EnumDealerChannelRateStatus {

    INIT(1, "初始化"),
    USEING(2, "通道使用中"),
    DELETE(3, "通道删除"),
    UNUSEING(4, "通道未使用");

    @Getter
    private int id;
    @Getter
    private String name;

    EnumDealerChannelRateStatus(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
