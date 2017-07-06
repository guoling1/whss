package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/6/12.
 */
public enum OperateType {
    CREATE("CREATE","注册会员"),
    RECHARGE("RECHARGE","会员充值"),
    ;
    public String key;
    public String value;

    OperateType(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
