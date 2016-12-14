package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/9.
 *
 * 校验身份4要素 记录状态
 */
public enum EnumVerifyID4ElementRecordStatus {

    EFFECTIVE(1, "有效"),

    INEFFECTIVE(2, "无效");

    @Getter
    private int id;
    @Getter
    private String msg;

    EnumVerifyID4ElementRecordStatus(final int id, final String msg) {
        this.id = id;
        this.msg = msg;
    }
}
