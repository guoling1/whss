package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 打款渠道
 */
public enum EnumUpperChannel {
    /**
     * 扫米
     */
    SAOMI(1, "扫米"),

    /**
     * 摩宝
     */
    MOBAO(2, "摩宝"),

    /**
     * 卡盟
     */
    KAMENG(3, "卡盟")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumUpperChannel(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}

