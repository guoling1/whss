package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
public enum EnumSettlementPeriodType {

    D0("D0"),

    TO("T0"),

    T1("T1");

    @Getter
    private String id;

    EnumSettlementPeriodType(final String id) {
        this.id = id;
    }
}
