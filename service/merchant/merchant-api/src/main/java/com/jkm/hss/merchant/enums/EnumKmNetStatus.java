package com.jkm.hss.merchant.enums;

import lombok.Getter;


public enum EnumKmNetStatus {
    /**
     * 成功
     */
    SUCCESS(1),

    /**
     * 失败
     */
    FAIL(2);

    @Getter
    private int id;
    EnumKmNetStatus(int id) {
        this.id = id;
    }
}
