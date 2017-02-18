package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-02-13.
 */
public enum EnumSplitBusinessType {

    HSSPAY("hssPay", "好收收- 收款"),

    HSSWITHDRAW("hssWithdraw", "好收收-提现"),

    HSSPROMOTE("hssUpgrade", "好收收-升级费"),

    HSYPAY("hsyPay", "好收银-收款");
    @Getter
    private String id;
    @Getter
    private String value;

    EnumSplitBusinessType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
