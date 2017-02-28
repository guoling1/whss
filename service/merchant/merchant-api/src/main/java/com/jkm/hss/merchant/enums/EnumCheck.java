package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/2/28.
 */
public enum EnumCheck {
    /**
     * 有
     */
    HAS(1),
    /**
     * 无
     */
    HASNOT(2);

    @Getter
    private int id;
    EnumCheck(int id) {
        this.id = id;
    }
}
