package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by Administrator on 2017/1/12.
 */
public enum EnumUserRole {


    /**
     * 法人
     */
    CORPORATE(1),
    /**
     * 店长
     */
    STOREMANAGER(2),
    /**
     * 店员
     */
    CLERK(3),

    /**
     * 财务
     */
    FINANCE(4);


    @Getter
    private int code;

    EnumUserRole(final int code) {
        this.code = code;
    }


}
