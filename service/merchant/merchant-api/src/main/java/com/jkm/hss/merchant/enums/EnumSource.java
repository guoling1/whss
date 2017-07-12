package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/3.
 */
public enum EnumSource {
    SCAN(0, "扫码注册"),

    RECOMMEND(1, "邀请注册"),

    DEALERRECOMMEND(2, "代理商邀请注册");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumSource(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
