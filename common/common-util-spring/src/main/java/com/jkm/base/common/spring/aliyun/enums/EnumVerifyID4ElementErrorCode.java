package com.jkm.base.common.spring.aliyun.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
public enum EnumVerifyID4ElementErrorCode {

    NORMAL("0", "正常"),

    EMPTY_BANK_CARD("201", "银行卡号为空"),

    EMPTY_REAL_NAME("202", "真实姓名为空"),

    ERROR_BANK_CARD("203", "银行卡号不正确"),

    ERROR_REAL_NAME("204", "真实姓名包含特殊字符"),

    ERROR_ID("205", "身份证不正确"),

    NO_MESSAGE("210", "没有信息");

    @Getter
    private String code;
    @Getter
    private String msg;

    EnumVerifyID4ElementErrorCode(final String code, final String msg) {
        this.code = code;
        this.msg = msg;
    }

    private static final ImmutableMap<String, EnumVerifyID4ElementErrorCode> INITMAP;

    static {
        final ImmutableMap.Builder<String, EnumVerifyID4ElementErrorCode> builder = new ImmutableMap.Builder<>();
        for (EnumVerifyID4ElementErrorCode errorCode : EnumVerifyID4ElementErrorCode.values()) {
            builder.put(errorCode.getCode(), errorCode);
        }
        INITMAP = builder.build();
    }

    public static EnumVerifyID4ElementErrorCode of (final String code) {
        Preconditions.checkState(null != INITMAP.get(code), "get VerifyID4ElementErrorCode error by code[{}]", code);
        return INITMAP.get(code);
    }
}
