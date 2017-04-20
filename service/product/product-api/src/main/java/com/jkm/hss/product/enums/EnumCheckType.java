package com.jkm.hss.product.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2017-04-11.
 */
public enum EnumCheckType {

    THREE_CHECK(3,"三要素"),

    FOUR_CHECK(4,"四要素"),

    FIVE_CHECK(5,"五要素"),

    SIX_CHECK(6,"六要素");

    @Getter
    private int id;
    @Getter
    private String msg;

    EnumCheckType(final int id, final String msg){

        this.id = id;
        this.msg = msg;
    }

    private static final ImmutableMap<Integer, EnumCheckType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumCheckType> builder = new ImmutableMap.Builder<>();
        for (EnumCheckType type : EnumCheckType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumCheckType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown type[{}]", type);
        return INIT_MAP.get(type);
    }
}
