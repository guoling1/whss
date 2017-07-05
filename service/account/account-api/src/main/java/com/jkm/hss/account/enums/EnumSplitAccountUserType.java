package com.jkm.hss.account.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/2/16.
 *
 * 分账方用户类型
 */
public enum EnumSplitAccountUserType {

    /**
     * 金开门
     */
    JKM(1, EnumAccountUserType.COMPANY, "金开门"),
    /**
     * 商户
     */
    MERCHANT(2, EnumAccountUserType.MERCHANT, "商户"),
    /**
     * 一级代理商
     */
    FIRST_DEALER(3, EnumAccountUserType.DEALER, "一级代理商"),
    /**
     * 二级代理商
     */
    SECOND_DEALER(4, EnumAccountUserType.DEALER, "二级代理商"),
    /**
     * 分公司
     */
    BRANCH_COMPANY(5, EnumAccountUserType.BRANCH_COMPANY, "分公司"),
    ;

    @Getter
    private int id;
    @Getter
    private EnumAccountUserType accountUserType;
    @Getter
    private String value;

    EnumSplitAccountUserType(final int id, final EnumAccountUserType accountUserType, final String value) {
        this.id = id;
        this.accountUserType = accountUserType;
        this.value = value;
    }
}
