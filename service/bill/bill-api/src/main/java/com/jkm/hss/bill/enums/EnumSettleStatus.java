package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 结算状态
 */
public enum EnumSettleStatus {

    /**
     * 待结算
     */
    DUE_SETTLE(1, "待结算"),
    /**
     *结算中
     */
    SETTLE_ING(2, "结算中"),
    /**
     *已结算
     */
    SETTLED(3, "已结算"),

    /**
     * 部分结算
     */
    SETTLE_PART(4, "部分结算")
    ;



    @Getter
    private int id;

    @Getter
    private String value;

    EnumSettleStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumSettleStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumSettleStatus> builder = new ImmutableMap.Builder<>();
        for (EnumSettleStatus status : EnumSettleStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSettleStatus of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
