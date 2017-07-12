package com.jkm.base.common.enums;

/**
 * Created by xingliujie on 2017/2/9.
 */
public enum EnumGlobalDealerLevel {
    FIRSTDEALER(1),
    SECORDDEALER(2),
    OEM(3);

    private final int value;
    EnumGlobalDealerLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
