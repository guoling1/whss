package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/5/2.
 */
public enum EnumSignCode {
    PAY(101, "收单分润"),
    WITHDRAW(102, "提现分润");



    @Getter
    private int id;
    @Getter
    private String name;
    EnumSignCode(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
