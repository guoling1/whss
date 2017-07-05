package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/9.
 */
public enum EnumProductType {
    HSS("hss", "好收收"),
    HSY("hsy", "好收银");


    @Getter
    private String id;
    @Getter
    private String name;

    EnumProductType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }

    private static final ImmutableMap<String, EnumProductType> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<String, EnumProductType> builder = new ImmutableMap.Builder<>();
        for (final EnumProductType status : EnumProductType.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }
    public static EnumProductType of(final String type) {
        return STATUS_IMMUTABLE_MAP.get(type);
    }
}
