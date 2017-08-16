package com.jkm.hss.merchant.enums;

/**
 * Created by Allen on 2017/8/15.
 */
public enum EnumMessageType {
    BENEFIT_MESSAGE(1,"分润消息"),
    VERIFY_MESSAGE(2,"审核消息"),
    PAY_MESSAGE(3,"收款消息"),
    WITHDRAW_MESSAGE(4,"提现消息"),
    ;

    public Integer key;
    public String value;
    EnumMessageType(Integer key,String value) {
        this.key=key;
        this.value=value;
    }
}
