package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 * 推广好友
 * tb_recommend
 */
public enum EnumRecommendType {
    DIRECT(1, "直接推广人"),

    INDIRECT(2, "间接推广人");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumRecommendType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
