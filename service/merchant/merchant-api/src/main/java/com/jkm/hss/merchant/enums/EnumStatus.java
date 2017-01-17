package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumStatus {
    /**
     * 正常记录
     */
    NORMAL(1),
    /**
     * 禁用记录
     */
    DISABLE(2);

    @Getter
    private int id;
    EnumStatus(int id) {
        this.id = id;
    }
}
