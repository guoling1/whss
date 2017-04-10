package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/3/21.
 */
public enum EnumChangeType {
    /**
     * BOSS
     */
    BOSS(1),

    /**
     * 一代
     */
    FIRSTDEALER(2),
    /**
     * 二代
     */
    SECONDDEALER(3);

    @Getter
    private int id;
    EnumChangeType(int id) {
        this.id = id;
    }
}
