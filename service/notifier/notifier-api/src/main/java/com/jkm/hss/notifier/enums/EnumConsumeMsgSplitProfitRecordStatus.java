package com.jkm.hss.notifier.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
public enum EnumConsumeMsgSplitProfitRecordStatus {

    /**
     * 待发送
     */
    PENDING_SEND(1, "待发送"),
    /**
     * 已发送
     */
    SUCCESS_SEND(2, "已发送")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumConsumeMsgSplitProfitRecordStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
