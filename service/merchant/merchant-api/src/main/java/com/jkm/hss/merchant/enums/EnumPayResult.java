package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 16:26
 */
public enum EnumPayResult {
    /**
     * 待支付
     */
    UNPAY("N", "待支付"),
    /**
     * 处理中
     */
    HAND("H", "支付中"),
    /**
     * 交易受理
     */
    ACCEPT("A", "交易受理"),

    /**
     * 支付成功
     */
    SUCCESS("S", "交易成功"),

    /**
     * 支付失败
     */
    FAIL("F", "交易失败"),

    /**
     * 等待中
     */
    WAIT("W", "提现等待中"),

    /**
     * 提现异常
     */
    EXCEPTION("E", "提现异常");


    @Getter
    private String id;
    @Getter
    private String name;

    EnumPayResult(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
