package com.jkm.hss.admin.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/2/22.
 */
public enum EnumAdminType {
    /**
     * boss后台
     */
    BOSS(1);


    @Getter
    private int code;

    EnumAdminType(final int code) {
        this.code = code;
    }
}
