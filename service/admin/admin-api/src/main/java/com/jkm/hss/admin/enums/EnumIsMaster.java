package com.jkm.hss.admin.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/2/22.
 */
public enum EnumIsMaster {
    /**
     * 有全部菜单权限
     */
    MASTER(1),
    /**
     * 没有全部菜单权限
     */
    NOTMASTER(2);


    @Getter
    private int code;

    EnumIsMaster(final int code) {
        this.code = code;
    }
}
