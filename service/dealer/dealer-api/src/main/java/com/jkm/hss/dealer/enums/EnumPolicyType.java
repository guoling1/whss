package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * 政策模板
 */
public enum EnumPolicyType {

    wechat("wechat", "微信"),
    alipay("alipay", "支付宝"),
    withdraw("withdraw", "提现");

    @Getter
    private String id;
    @Getter
    private String name;

    EnumPolicyType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
