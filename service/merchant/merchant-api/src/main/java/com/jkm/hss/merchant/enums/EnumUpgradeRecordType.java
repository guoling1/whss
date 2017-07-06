package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-29 16:31
 */
public enum EnumUpgradeRecordType {
    /**
     * 交钱
     */
    RECHARGE(1),

    /**
     * 推广
     */
    RECOMMEND(2);

    @Getter
    private int id;

    EnumUpgradeRecordType(final int id) {
        this.id = id;
    }
}
