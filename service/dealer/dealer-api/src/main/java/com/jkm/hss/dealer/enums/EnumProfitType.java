package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-25.
 */
public enum EnumProfitType {

    BALANCE(1, "收单分润"),
    WITHDRAW(2, "提现分润"),
    INDIRECTWITHDRAW(3, "推荐商户提现分润");


    @Getter
    private int id;
    @Getter
    private String name;
    EnumProfitType(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
