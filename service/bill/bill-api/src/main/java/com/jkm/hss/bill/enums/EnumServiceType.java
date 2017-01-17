package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/1/12.
 *
 * 业务类型
 */
public enum EnumServiceType {

    /**
     * 收款
     */
    RECEIVE_MONEY(1, "收款"),

    /**
     * 增值付费
     */
    APPRECIATION_PAY(2, "增值付费"),
    ;



    @Getter
    private int id;

    @Getter
    private String value;

    EnumServiceType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumServiceType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumServiceType> builder = new ImmutableMap.Builder<>();
        for (EnumServiceType type : EnumServiceType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumServiceType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown status[{}]", type);
        return INIT_MAP.get(type);
    }
}
