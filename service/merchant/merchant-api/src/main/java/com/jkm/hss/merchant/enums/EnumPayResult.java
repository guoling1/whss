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
    UNPAY("N", "待支付/待提现"),
    /**
     * 处理中
     */
    HAND("H", "支付中/提现中"),

    /**
     * 支付成功
     */
    SUCCESS("S", "支付成功/提现成功"),

    /**
     * 支付失败
     */
    FAIL("F", "交易失败/提现失败"),
    /**
     * 受理成功
     */
    ACCEPT("A","受理成功"),


    /**
     * 等待中
     */
    WAIT("W", "提现等待中"),

    /**
     * 提现异常
     */
    EXCEPTION("E", "支付异常/提现异常"),


    /**
     * 审核不通过
     */
    UNPASS("O", "审核未通过"),

    /**
     * 交易关闭
     */
    DOWN("D", "交易关闭");
    @Getter
    private String id;
    @Getter
    private String name;

    EnumPayResult(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
