package com.jkm.api.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/8/16.
 */
public enum EnumApiOrderStatus {

    INIT("INIT", "初始化"),

    PROCESSING("PROCESSING", "处理中"),

    SUCCESS("SUCCESS", "成功"),

    FAIL("FAIL", "失败")
    ;
    @Getter
    private String code;
    @Getter
    private String value;

    EnumApiOrderStatus(final String code, final String value) {
        this.code = code;
        this.value = value;
    }
}
