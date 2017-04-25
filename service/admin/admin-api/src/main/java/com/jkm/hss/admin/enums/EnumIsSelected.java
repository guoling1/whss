package com.jkm.hss.admin.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/3/27.
 */
public enum  EnumIsSelected {
    /**
     * 有全部菜单权限
     */
    SELECTED(1),
    /**
     * 没有全部菜单权限
     */
    UNSELECTED(0);


    @Getter
    private int code;

    EnumIsSelected(final int code) {
        this.code = code;
    }
}
