package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-29 16:31
 */
public enum EnumSettleStatus {
    /**
     * 已结算
     */
    SETTLE(0),
    /**
     * 未结算
     */
    UNSETTLE(1);

    @Getter
    private int id;

    EnumSettleStatus(final int id) {
        this.id = id;
    }
}
