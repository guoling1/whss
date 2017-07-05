package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumMerchantType {
    MERCHANT(0),
    AGENT(1);

    @Getter
    private int id;
    EnumMerchantType(int id) {
        this.id = id;
    }
}
