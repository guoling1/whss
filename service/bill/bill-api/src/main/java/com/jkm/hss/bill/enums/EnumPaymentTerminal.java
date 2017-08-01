package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/8/1.
 *
 * 支付终端枚举
 */
public enum  EnumPaymentTerminal {

    CELLPHONE(1, "手机端"),

    PC(2, "pc端"),

    POS(3, "pos端")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumPaymentTerminal (final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
