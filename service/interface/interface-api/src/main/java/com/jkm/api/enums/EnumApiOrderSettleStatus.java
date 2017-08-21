package com.jkm.api.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/8/16.
 */
public enum EnumApiOrderSettleStatus {

    WAIT("WAIT", "待结算"),

    PROCESSING("PROCESSING", "处理中"),

    SUCCESS("SUCCESS", "成功"),

    FAIL("FAIL", "失败")
    ;
    @Getter
    private String code;
    @Getter
    private String value;

    EnumApiOrderSettleStatus(final String code, final String value) {
        this.code = code;
        this.value = value;
    }
}
