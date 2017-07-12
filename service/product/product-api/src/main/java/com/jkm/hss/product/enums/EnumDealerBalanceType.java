package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-25.
 */
public enum EnumDealerBalanceType {

    /**
     * D0
     */
    D0("D0", " D0"),

    /**
     * 日结
     */
    EVERYDAY("D1", "日结"),

    /**
     * 月结
     */
    EVERYMONTH("M1", "月结");

    @Getter
    private String id;
    @Getter
    private String name;

    EnumDealerBalanceType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
