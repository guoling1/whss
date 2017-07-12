package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/9.
 */
public enum EnumUpgradePayResult {
    /**
     * 待支付
     */
    UNPAY("N","待支付"),
    /**
     * 支付成功
     */
    SUCCESS("S", "支付成功"),
    /**
     * 支付失败
     */
    FAIL("F", "支付失败");
    @Getter
    private String id;
    @Getter
    private String name;

    EnumUpgradePayResult(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

}
