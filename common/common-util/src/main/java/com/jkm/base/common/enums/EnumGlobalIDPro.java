package com.jkm.base.common.enums;

/**
 * Created by lk on 29/12/2016.
 */
public enum EnumGlobalIDPro {
    MAX(1),
    MIN(2);

    private final int value;
    EnumGlobalIDPro(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
