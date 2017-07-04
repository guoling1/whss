package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/5/19.
 */
public enum MemberStatus {
    ACTIVE(1,"正常"),
    NOT_ACTIVE_FOR_RECHARGE(2,"未充值无法使用"),
    ;
    public int key;
    public String value;

    MemberStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
