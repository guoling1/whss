package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/5/23.
 */
public enum RechargeValidType {
    ACTIVATE("activate","开卡充值"),
    RECHARGE("recharge","正常充值"),
    ;
    public String key;
    public String value;

    RechargeValidType(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
