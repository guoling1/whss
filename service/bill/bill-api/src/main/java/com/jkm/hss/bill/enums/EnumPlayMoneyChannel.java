package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 打款渠道
 */
public enum  EnumPlayMoneyChannel {


    SAOMI(1, "扫米");


    @Getter
    private int id;
    @Getter
    private String value;

    EnumPlayMoneyChannel(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}

