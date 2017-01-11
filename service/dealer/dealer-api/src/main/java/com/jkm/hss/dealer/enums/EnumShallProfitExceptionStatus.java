package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-12-21.
 */
public enum EnumShallProfitExceptionStatus {

    EXCEPTION(1),
    HANDLE(2);

    @Getter
    private int id;
    EnumShallProfitExceptionStatus(int id) {
        this.id = id;
    }
}
