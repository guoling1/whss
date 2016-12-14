package com.jkm.base.common.spring.aliyun.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
public enum EnumVerifyID4ElementStatus {

    PASS("0"),

    NOT_PASS("1");

    @Getter
    private String code;

    EnumVerifyID4ElementStatus(final String code) {
        this.code = code;
    }
}
