package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * 商户状态
 */
public enum EnumMerchantStatus {

    /**
     * 初始化
     */
    LOGIN(-1, "登录"),
    /**
     * 初始化
     */
    INIT(0, "初始化"),
    /**
     * 初始化
     */
    ONESTEP(1, "填写完第一步"),
    /**
     * 待审核
     */
    REVIEW(2, "待审核"),

    /**
     * 审核通过
     */
    PASSED(3, "审核通过"),

    /**
     * 审核未通过
     */
    UNPASSED(4, "审核未通过"),
    /**
     * 禁用
     */
    DISABLE(5, "禁用"),
    /**
     * 消费达到指定数额，成为真实好友
     */
    FRIEND(6, "消费达到指定数额，成为真实好友");


    @Getter
    private int id;
    @Getter
    private String name;

    EnumMerchantStatus(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

}
