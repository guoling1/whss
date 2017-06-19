package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-02-27.
 */
public enum EnumMerchantPayType {

    MERCHANT_CODE("code", "扫码支付"),

    MERCHANT_JSAPI("jsapi", "公众号支付"),

    MERCHANT_BAR("bar", "主扫");

    @Getter
    private String id;

    @Getter
    private String msg;

    EnumMerchantPayType(String id, String msg){

        this.id = id;
        this.msg = msg;
    }
}
