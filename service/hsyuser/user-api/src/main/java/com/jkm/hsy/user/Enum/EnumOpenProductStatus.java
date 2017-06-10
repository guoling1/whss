package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/4/18.
 */
public enum EnumOpenProductStatus {
    PASS(1, "开通产品成功"),

    UNPASS(2, "开通产品失败"),

    HANDLING(3, "已提交");
    @Getter
    private int id;
    @Getter
    private String value;

    EnumOpenProductStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
