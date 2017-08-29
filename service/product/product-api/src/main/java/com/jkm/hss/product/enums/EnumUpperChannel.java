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
     * 厦门民生银行
     */
    XMMS_BANK(7, "XMMS","厦门民生"),

    /**
     * 收银家。
     */
    SYJ(8, "SYJ", "厦门华夏"),

    /**
     * 合利宝
     */
    HE_LI_UNIONPAY(10, "HLB", "合利宝"),


    /**
     * 汇聚支付
     */
    HJ_PAY(11, "HJ_PAY","汇聚"),

    /**
     * 会员卡
     */
    MEMBER(12, "MEMBER", "会员卡"),

    /**
     * 微信官方支付
     */
    WECHAT(13, "WECHAT","微信官方支付"),

    JH_UNIONPAY(14, "JH_UNIONPAY", "玖和快捷");

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

    /**
     * 是否需要保持银行卡（部分渠道需要卡流程已经保存，部分只有支付成功才保存）
     *
     * @param channelId
     * @return
     */
    public static boolean isNotNeedSaveBankCard(final int channelId) {
        return JH_UNIONPAY.getId() == channelId;
    }

    /**
     * 是否需要计算结算金额（自动结算的渠道，部分渠道需要）
     *
     * @param channelId
     * @return
     */
    public static boolean isNeedCalculate(final int channelId) {
        return JH_UNIONPAY.getId() == channelId;
    }
}

