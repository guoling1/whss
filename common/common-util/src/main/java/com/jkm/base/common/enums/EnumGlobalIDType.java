package com.jkm.base.common.enums;

/**
 * Created by lk on 29/12/2016.
 */
public enum EnumGlobalIDType {
    MERCHANT(1),
    USER(2),
    DEALER(3),
    OEM(4);

    private final int value;
    EnumGlobalIDType(int value) {
        this.value = value;
    }

    public int getValue(){
        return value;
    }

}

