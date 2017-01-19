package com.jkm.hss.admin.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/16.
 * 系统类型
 */
public enum EnumQRCodeSysType {
    /**
     * 好收收
     */
    HSS("hss", "好收收"),

    /**
     * 好收银
     */
    HSY("hsy", "好收银");



    @Getter
    private String id;

    @Getter
    private String value;

    EnumQRCodeSysType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<String, EnumQRCodeSysType> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumQRCodeSysType> builder = new ImmutableMap.Builder<>();
        for (EnumQRCodeSysType type : EnumQRCodeSysType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumQRCodeSysType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown status[{}]", type);
        return INIT_MAP.get(type);
    }

}
