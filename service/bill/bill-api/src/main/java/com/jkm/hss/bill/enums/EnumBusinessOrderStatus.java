package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/5/18.
 */
public enum EnumBusinessOrderStatus {

    /**
     * 待支付
     */
    DUE_PAY(1, "待支付"),

    /**
     *支付失败
     */
    PAY_FAIL(2, "支付失败"),

    /**
     *支付成功
     */
    PAY_SUCCESS(3, "支付成功"),

    /**
     *提现中
     */
    WITHDRAWING(4, "提现中"),

    /**
     *提现成功
     */
    WITHDRAW_SUCCESS(5, "提现成功"),

    /**
     *充值成功
     */
    RECHARGE_SUCCESS(6, "充值成功"),

    /**
     *充值失败
     */
    RECHARGE_FAIL(7, "充值失败")

    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumBusinessOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
