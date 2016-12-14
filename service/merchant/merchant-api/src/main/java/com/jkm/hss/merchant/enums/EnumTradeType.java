package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-29 16:31
 */
public enum EnumTradeType {
    /**
     * 交易
     */
    DEAL(0),

    /**
     * 体现
     */
    DEPOSITOR(1);

    @Getter
    private int id;

    EnumTradeType(final int id) {
        this.id = id;
    }
}
