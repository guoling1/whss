package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumPlatformType {

    /**
     * boss后台
     */
    BOSS("boss", "boss后台"),

    /**
     * 代理商后台
     */
    DEALER("dealer", "代理商后台"),

    /**
     * 报单后台
     */
    BAODAN("baodan", "报单后台");
    @Getter
    private String id;
    @Getter
    private String name;

    EnumPlatformType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
}
