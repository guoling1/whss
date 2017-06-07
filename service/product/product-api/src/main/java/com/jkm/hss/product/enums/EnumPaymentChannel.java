package com.jkm.hss.product.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 支付渠道
 */
public enum EnumPaymentChannel {


    /**
     * 微信
     */
    WECHAT_PAY(1, "微信"),

    /**
     * 支付宝
     */
    ALIPAY(2, "支付宝"),

    /**
     * 快捷
     */
    UNIONPAY(3, "快捷"),

    QQPAY(4, "QQ钱包"),

    /**
     * 银联扫码
     */
    UNIONPAY_CODE(5,"银联扫码");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumPaymentChannel(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumPaymentChannel> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumPaymentChannel> builder = new ImmutableMap.Builder<>();
        for (EnumPaymentChannel status : EnumPaymentChannel.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumPaymentChannel of(final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }

}
