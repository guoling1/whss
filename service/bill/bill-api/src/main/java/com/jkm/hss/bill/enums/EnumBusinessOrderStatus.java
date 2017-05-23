package com.jkm.hss.bill.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/5/18.
 */
public enum EnumBusinessOrderStatus {

    /**
     * 待支付
     */
    DUE_PAY(1, "待支付"),

    /**
     *支付失败
     */
    PAY_FAIL(2, "支付失败"),

    /**
     *支付成功
     */
    PAY_SUCCESS(3, "支付成功")

    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumBusinessOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumBusinessOrderStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumBusinessOrderStatus> builder = new ImmutableMap.Builder<>();
        for (EnumBusinessOrderStatus status : EnumBusinessOrderStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }
}
