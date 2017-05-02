package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/4/18.
 */
public enum EnumHxbsOpenProductStatus {
    PASS(1, "开通产品成功"),

    UNPASS(2, "开通产品失败");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumHxbsOpenProductStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
