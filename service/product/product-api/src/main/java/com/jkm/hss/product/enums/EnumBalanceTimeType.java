package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-25.
 */
public enum EnumBalanceTimeType {

    D0("D0", ""),
    D1("D1", ""),
    TO("T0", ""),
    T1("T1", "");

    /**
     * id enum map
     */
    private static final ImmutableMap<String, EnumBalanceTimeType> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<String, EnumBalanceTimeType> builder = new ImmutableMap.Builder<>();
        for (final EnumBalanceTimeType status : EnumBalanceTimeType.values()) {
            builder.put(status.getType(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private String type;
    @Getter
    private String name;

    EnumBalanceTimeType(final String type, final String name) {
        this.type = type;
        this.name = name;
    }

    /**
     * int convert to enum
     *
     * @param
     * @return enum
     */
    public static EnumBalanceTimeType of(final String type) {
        return STATUS_IMMUTABLE_MAP.get(type);
    }
}
