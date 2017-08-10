package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-07-24.
 */
public enum EnumWithdrawOrderStatus {

    /**
     *提现中
     */
    WAIT_WITHDRAW(1, "待提现"),

    /**
     *提现中
     */
    WITHDRAWING(2, "提现中"),

    /**
     *提现成功
     */
    WITHDRAW_SUCCESS(3, "提现成功"),

    /**
     *提现中
     */
    WITHDRAW_FAIL(4, "提现失败"),

    WITHDRAW_WAIT(5,"提现挂起");

    @Getter
    private int code;
    @Getter
    private String msg;

    EnumWithdrawOrderStatus(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
