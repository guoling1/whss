package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 支付方式
 */
public enum EnumPaymentType {

    /**
     * 微信公众号
     */
//    WECHAT_SUBSCRIPTION(1, "微信公众号"),

    /**
     * 微信扫码
     */
    WECHAT_SCAN_CODE("S", "微信扫码"),

    /**微信二维码
     *
     */
    WECHAT_QR_CODE("N", "微信二维码"),

    /**
     * 微信H5收银台
     */
    WECHAT_H5_CASHIER_DESK("H", "微信H5收银台"),

    /**
     * 无卡快捷支付
     */
    QUICK_APY("B", "快捷收款"),

    /**
     * 支付宝扫码
     */
    ALIPAY_SCAN_CODE("Z", "支付宝扫码")
    ;



    @Getter
    private String id;
    @Getter
    private String value;

    EnumPaymentType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
