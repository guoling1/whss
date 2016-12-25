package com.jkm.hss.account.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
public enum EnumAccountFlowType {


    INCREASE(1, "增加"),

    DECREASE(2, "减少");

    @Getter
    private final int id;
    @Getter
    private final String value;

    EnumAccountFlowType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
