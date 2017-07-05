package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/23.
 *
 * dealerPassport 类型
 */
public enum EnumPassportType {

    PC(1),
    MOBILE(2);

    @Getter
    private int id;

    EnumPassportType(int id) {
            this.id = id;
        }
}
