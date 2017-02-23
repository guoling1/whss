package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/2/22.
 */
public enum EnumSettleDestinationType {

    /**
     * 结算到卡
     */
    TO_CARD(1, "结算到卡"),

    /**
     * 结算到余额
     */
    TO_ACCOUNT(2, "结算到余额")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumSettleDestinationType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumSettleDestinationType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumSettleDestinationType> builder = new ImmutableMap.Builder<>();
        for (EnumSettleDestinationType type : EnumSettleDestinationType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSettleDestinationType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown type[{}]", type);
        return INIT_MAP.get(type);
    }
}
