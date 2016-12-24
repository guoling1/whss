package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 交易类型
 */
public enum EnumTradeType {

    /**
     * 支付
     */
    PAY(1, "支付"),

    /**
     * 充值
     */
    RECHARGE(2, "充值"),

    /**
     * 提现
     */
    WITHDRAW(3, "提现");


    @Getter
    private int id;

    @Getter
    private String value;

    EnumTradeType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
