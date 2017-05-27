package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/5/2.
 */
public enum EnumOemType {
    /**
     * 代理商
     */
    DEALER(0),
    /**
     * 分公司
     */
    OEM(1);

    @Getter
    private int id;
    EnumOemType(int id) {
        this.id = id;
    }
}
