package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public enum EnumPhotoType {
    BANKPIC(1, "结算卡"),

    BANKHANDPIC(2, "手持结算卡"),

    IDENTITYHANDPIC(3, "手持身份证"),

    IDENTITYFACEPIC(4, "身份证正面"),

    PicIDENTITYOPPOSITEPIC(5, "身份证反面");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumPhotoType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
