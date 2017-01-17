package com.jkm.hss.account.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/1/12.
 */
public enum EnumAppType {

    /**
     * 好收收
     */
    HSS("hss", "好收收"),

    /**
     * 好收银
     */
    HSY("hsy", "好收银"),
    ;



    @Getter
    private String id;

    @Getter
    private String value;

    EnumAppType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<String, EnumAppType> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumAppType> builder = new ImmutableMap.Builder<>();
        for (EnumAppType type : EnumAppType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumAppType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown status[{}]", type);
        return INIT_MAP.get(type);
    }
}
