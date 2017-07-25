package com.jkm.hss.settle.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-07-21.
 */
public enum EnumSettleExceptionStatus {


    DUE_HANDLE(0, "待处理"),

    HANDLE_ING(1,"处理中"),

    HANDLED(2, "已处理");

    @Getter
    private int id;

    @Getter
    private String msg;

    EnumSettleExceptionStatus(int id, String msg){
        this.id = id;
        this.msg = msg;
    }
}
