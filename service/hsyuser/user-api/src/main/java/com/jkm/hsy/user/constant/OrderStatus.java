package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/5/23.
 */
public enum OrderStatus {
    NEED_RECHARGE(0,"待充值"),
    RECHARGE_SUCCESS(1,"充值成功"),
    RECHARGE_FAIL(2,"充值失败"),
    ;
    public int key;
    public String value;

    OrderStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
