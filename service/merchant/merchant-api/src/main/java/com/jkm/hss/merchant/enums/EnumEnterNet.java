package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumEnterNet {
    /**
     * 1 未入网
     */
    UNENT(1),
    /**
     * 2 已入网
     */
    HASENT(2);

    @Getter
    private int id;
    EnumEnterNet(int id) {
        this.id = id;
    }
}
