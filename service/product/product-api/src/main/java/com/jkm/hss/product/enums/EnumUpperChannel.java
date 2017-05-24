package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 打款渠道
 */
public enum EnumUpperChannel {
    /**
     * 扫米
     */
    SAOMI(1, "SM", "扫米"),

    /**
     * 摩宝
     */
    MOBAO(2, "MB", "摩宝"),

    /**
     * 卡盟
     */
    KAMENG(3, "KM", "卡盟"),

    /**
     * 合众易宝
     */
    HEZONG_YIBAO(4, "HZYB", "合众易宝"),

    /**
     * 溢+
     */
    YIJIA(5, "YIJIA", "溢+"),

    /**
     * 易联
     */
    EASY_LINK(6, "EL", "易联"),

    /**
     * 民生银行
     */
    MS_BANK(7, "MS", "民生银行"),

    /**
     * 收银家
     */
    SYJ(8, "SYJ", "收银家"),


    /**
     * 合利宝
     */
    HE_LI_UNIONPAY(10, "HLB", "合利宝")
    ;

    @Getter
    private int id;
    @Getter
    private String code;
    @Getter
    private String value;

    EnumUpperChannel(final int id, final String code, final String value) {
        this.id = id;
        this.code = code;
        this.value = value;
    }
}

