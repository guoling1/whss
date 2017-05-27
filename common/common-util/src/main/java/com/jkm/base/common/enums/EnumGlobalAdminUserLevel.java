package com.jkm.base.common.enums;

/**
 * Created by xingliujie on 2017/2/9.
 */
public enum EnumGlobalAdminUserLevel {
    BOSS(100),
    FIRSTDEALER(110),
    SECONDDEALER(120),
    OEM(130);

    private final int value;
    EnumGlobalAdminUserLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
