package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/29.
 */
public enum EnumHsyPhotoType {
    IDCARDF(1, "身份证正面"),

    IDCARDB(2, "身份证反面"),

    LICENCEID(3, "营业执照"),

    STOREFRONTID(4, "店面照片"),

    COUNTERID(5, "收银台"),

    INDOORID(6, "室内照片"),

    IDCARDC(7, "结算卡正面"),

    CONTRACTID(8, "签约合同照");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumHsyPhotoType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
