package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by huangwei on 3/2/16.
 */
public enum EnumPassportStatus {
    INVALID(0),
    VALID(1);

    @Getter
    private int id;

    EnumPassportStatus(int id) {
        this.id = id;
    }
}
