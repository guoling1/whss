package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/5/2.
 */
public enum EnumSignCode {
    AUDITED(101, "审核通过"),
    NOTTHROUGH(102, "审核不通过"),
    RECEIPT(103, "收款成功"),
    WITHDRAW(104, "提现到账");



    @Getter
    private int id;
    @Getter
    private String name;
    EnumSignCode(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
