package com.jkm.hsy.user.Enum;

import lombok.Getter;

/**
 * Created by wayne on 17/5/22.
 * 广告状态
 */
public enum EnumAdvertStatus {

    Passing(1,"正在使用")
    ;
    @Getter
    private int id;
    @Getter
    private String value;

    EnumAdvertStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
}
