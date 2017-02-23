package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/2/22.
 *
 * 结算方式
 */
public enum EnumSettleModeType {

    /**
     * 自主结算
     */
    SELF_SETTLE(1, "自主结算"),
    /**
     * 渠道结算
     */
    CHANNEL_SETTLE(2, "渠道结算")
    ;


    @Getter
    private int id;
    @Getter
    private String value;


    EnumSettleModeType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumSettleModeType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumSettleModeType> builder = new ImmutableMap.Builder<>();
        for (EnumSettleModeType type : EnumSettleModeType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSettleModeType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown type[{}]", type);
        return INIT_MAP.get(type);
    }
}
