package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by longwen.jiang on 2017/1/12.
 */
public enum EnumUserStatus {


    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 待审核
     */
    PENDINGAUDIT(2),
    /**
     * 禁用
     */
    FORBIDDEN (99);




    @Getter
    private int code;

    EnumUserStatus(final int code) {
        this.code = code;
    }


}
