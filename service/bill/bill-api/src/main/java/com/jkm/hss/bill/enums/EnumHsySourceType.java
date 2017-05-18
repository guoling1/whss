package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by wayne on 17/5/17.
 */
public enum EnumHsySourceType {

    QRCODE(1,"二维码")
    ;
    @Getter
    private int id;
    @Getter
    private String value;

    EnumHsySourceType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
