package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-25.
 */
public enum EnumMerchantWithdrawType {


    /**
     * 手动体现
     */
    BYHAND("HAND", " 手动提现"),

    /**
     * 逐笔手动体现
     */
    AUTO("AUTO", "逐笔自动提现");

    @Getter
    private String id;
    @Getter
    private String name;

    EnumMerchantWithdrawType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
