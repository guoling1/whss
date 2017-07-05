package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/3/6.
 */
public enum EnumBankDefault {
    /**
     * 不是默认银行卡
     */
    UNDEFALUT(0),

    /**
     * 默认银行卡
     */
    DEFAULT(1);

    @Getter
    private int id;
    EnumBankDefault(int id) {
        this.id = id;
    }
}
