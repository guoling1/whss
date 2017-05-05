package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
public enum EnumRefundOrderStatus {

    /**
     * 退款中
     */
    REFUNDING(1, "退款中"),
    /**
     * 退款成功
     */
    REFUND_SUCCESS(2, "退款成功"),
    /**
     * 退款失败
     */
    REFUND_FAIL(3, "退款失败")
    ;

    ;
    @Getter
    private int id;
    @Getter
    private String value;

    EnumRefundOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
