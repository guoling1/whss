package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * 政策模板
 */
public enum EnumPolicyType {

    WECHAT("wechat", "微信"),
    ALIPAY("alipay", "支付宝"),
    WITHDRAW("withdraw", "提现");

    @Getter
    private String id;
    @Getter
    private String name;

    EnumPolicyType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
