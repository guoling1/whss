package com.jkm.hss.dealer.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by Thinkpad on 2016/12/30.
 */
public enum EnumDealerRateType {
    /**
     * 升级费分润
     */
    UPGRADE(1),

    /**
     * 收单分润
     */
    TRADE(2);

    /**
     * id enum map
     */
    private static final ImmutableMap<Integer, EnumDealerLevel> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumDealerLevel> builder = new ImmutableMap.Builder<>();
        for (final EnumDealerLevel status : EnumDealerLevel.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int id;

    EnumDealerRateType(final int id) {
        this.id = id;
    }

}
