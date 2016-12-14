package com.jkm.hss.dealer.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/22.
 *
 * 经销商级别
 */
public enum EnumDealerLevel {

    /**
     * 一级
     */
    FIRST(1),

    /**
     * 二级
     */
    SECOND(2),

    /**
     * 三级
     */
    THIRD(3);

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

    EnumDealerLevel(final int id) {
        this.id = id;
    }

    /**
     * int convert to enum
     *
     * @param id int
     * @return enum
     */
    public static EnumDealerLevel of(final int id) {
        return STATUS_IMMUTABLE_MAP.get(id);
    }

}
