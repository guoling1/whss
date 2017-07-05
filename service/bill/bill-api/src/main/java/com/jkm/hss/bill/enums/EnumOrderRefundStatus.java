package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/5/4.
 *
 * 交易退款状态
 */
public enum EnumOrderRefundStatus {

    /**
     *  未退款
     */
    UN_REFUND(0, "未退款"),
    /**
     * 已退完
     */
    REFUND_SUCCESS(1, "已退款"),
    /**
     * 部分退款
     */
    REFUND_PART(2, "部分退款")
    ;
    @Getter
    private int id;
    @Getter
    private String value;

    EnumOrderRefundStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumOrderRefundStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumOrderRefundStatus> builder = new ImmutableMap.Builder<>();
        for (EnumOrderRefundStatus status : EnumOrderRefundStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumOrderRefundStatus of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }

}
