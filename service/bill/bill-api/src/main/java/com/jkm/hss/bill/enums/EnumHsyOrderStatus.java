package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by wayne on 17/5/17.
 */
public enum EnumHsyOrderStatus {
    /**
     * 待支付
     */
    DUE_PAY(1, "待支付"),
    PAY_SUCCESS(2, "支付成功"),
    REFUNDING(3,"退款中"),
    REFUND_PART(4,"部分退款"),
    /**
     * 退款成功
     */
    REFUND_SUCCESS(5, "退款成功"),
    /**
     * 退款失败
     */
    REFUND_FAIL(6, "退款失败")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumHsyOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
