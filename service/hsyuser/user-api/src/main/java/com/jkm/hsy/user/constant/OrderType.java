package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/5/23.
 */
public enum OrderType {
    ACTIVATE(1,"开卡充值"),
    ACTIVATE_PRESENT(2,"开卡送"),
    RECHARGE(3,"充值"),
    RECHARGE_PRESENT(4,"充值送"),
    ;
    public int key;
    public String value;

    OrderType(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
