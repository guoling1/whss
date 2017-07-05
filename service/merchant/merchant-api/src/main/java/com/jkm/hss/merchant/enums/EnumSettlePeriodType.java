package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
public enum EnumSettlePeriodType {

    D0("D0"),

    TO("T0"),

    T1("T1");

    @Getter
    private String id;

    EnumSettlePeriodType(final String id) {
        this.id = id;
    }
}
