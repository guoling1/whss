package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/6/10.
 */
public enum EnumXmmsStatus {
    HANDLING("handling", "已提交"),

    FAIL("fail", "入网失败");

    @Getter
    private String id;
    @Getter
    private String value;

    EnumXmmsStatus(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
