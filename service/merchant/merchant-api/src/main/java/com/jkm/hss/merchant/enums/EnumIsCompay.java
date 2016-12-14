package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:代付对公或对私
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-23 11:28
 */
public enum EnumIsCompay {
    ToPublic("1", "对公"),

    ToPrivate("0", "对私");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumIsCompay(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
