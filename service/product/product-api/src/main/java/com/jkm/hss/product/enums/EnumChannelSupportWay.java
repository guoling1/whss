package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-02-27.
 */
public enum EnumChannelSupportWay {

    ONLY_CODE(1 , "扫码支付"),

    ONLY_JSAPI(2 , "公众号支付"),

    CODE_JSAPI(3 , "扫码，公众号支付");

    @Getter
    private int way;

    @Getter
    private String msg;

    EnumChannelSupportWay(final int way, final String msg){

        this.way = way;

        this.msg = msg;
    }
}
