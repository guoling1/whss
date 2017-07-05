package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/4/18.
 */
public enum EnumHxbsStatus {
    PASS(1, "入驻成功"),

    UNPASS(2, "入驻失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumHxbsStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
