package com.jkm.hss.admin.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/5/2.
 */
public enum EnumAdminOemType {
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
    EnumAdminOemType(int id) {
        this.id = id;
    }
}
