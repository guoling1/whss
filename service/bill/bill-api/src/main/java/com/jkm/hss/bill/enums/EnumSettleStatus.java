package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 结算状态
 */
public enum EnumSettleStatus {

    /**
     * 待结算
     */
    DUE_SETTLE(1, "待结算"),
    /**
     *结算中
     */
    SETTLE_ING(2, "结算中"),
    /**
     *已结算
     */
    SETTLED(3, "已结算")
    ;



    @Getter
    private int id;

    @Getter
    private String value;

    EnumSettleStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
