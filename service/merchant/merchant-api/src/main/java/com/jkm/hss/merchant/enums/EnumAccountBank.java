package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/2/28.
 */
public enum EnumAccountBank {
    /**
     * 借记卡
     */
    DEBITCARD(0),

    /**
     * 借贷卡
     */
    CREDIT(1);

    @Getter
    private int id;
    EnumAccountBank(int id) {
        this.id = id;
    }
}
