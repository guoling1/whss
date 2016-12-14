package com.jkm.hss.notifier.enums;

import lombok.Getter;

/**
 * Created by konglingxin on 3/11/15.
 * 用户类型
 */
@Getter
public enum EnumUserType {

    FOREGROUND_USER(1, "前端用户"),

    BACKGROUND_USER(2, "运营后台用户"),

    MERCHANT_USER(3, "商户后台用户");

    private int id;
    private String desc;

    /**
     * 构造函数
     *
     * @param id
     * @param desc
     */
    EnumUserType(final int id, final String desc) {
        this.id = id;
        this.desc = desc;
    }
}
