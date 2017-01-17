package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/30.
 */
public enum EnumUpgrade {
    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 禁用
     */
    DISABLE(2);
    /**
     * id enum map
     */
    private static final ImmutableMap<Integer, EnumUpgrade> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumUpgrade> builder = new ImmutableMap.Builder<>();
        for (final EnumUpgrade status : EnumUpgrade.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int id;

    EnumUpgrade(final int id) {
        this.id = id;
    }
}
