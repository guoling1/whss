package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumCommonStatus {
    /**
     * 正常记录
     */
    NORMAL(0),
    /**
     * 禁用记录
     */
    DISABLE(1);

    @Getter
    private int id;
    EnumCommonStatus(int id) {
        this.id = id;
    }
}
