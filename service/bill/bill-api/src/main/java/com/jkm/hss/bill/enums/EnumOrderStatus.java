package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 订单交易状态
 */
public enum  EnumOrderStatus {

    /**
     * 待支付
     */
    DUE_PAY(1, "待支付"),

    /**
     *支付中"
     */
//    PAYING(2, "支付中"),

    /**
     *支付失败
     */
    PAY_FAIL(3, "支付失败"),

    /**
     *支付成功
     */
    PAY_SUCCESS(4, "支付成功"),

    /**
     *提现中
     */
    WITHDRAWING(5, "提现中"),

    /**
     *提现成功
     */
    WITHDRAW_SUCCESS(6, "提现成功"),

    /**
     *充值成功
     */
    RECHARGE_SUCCESS(7, "充值成功"),

    /**
     *充值失败
     */
    RECHARGE_FAIL(8, "充值失败")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
