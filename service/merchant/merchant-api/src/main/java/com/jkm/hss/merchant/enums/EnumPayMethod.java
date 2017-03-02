package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/3/1.
 */
public enum EnumPayMethod {

    /**
     * 微信
     */
    WEIXIN("微信", "微信"),


    /**
     * 支付宝
     */
    ALIPAY("支付宝", "支付宝"),

    /**
     * 银行
     */
    FASTPAY("银行", "银行");
    @Getter
    private String id;
    @Getter
    private String name;

    EnumPayMethod(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
