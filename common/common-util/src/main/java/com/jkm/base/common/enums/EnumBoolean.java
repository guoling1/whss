package com.jkm.base.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangwei on 5/5/16.
 * 布尔枚举
 */
public enum EnumBoolean {
    TRUE(1),
    FALSE(0);

    private static final Map<Integer, EnumBoolean> ENUM_MAP = new HashMap<>();

    static {
        for (final EnumBoolean enumBoolean : EnumBoolean.values()) {
            ENUM_MAP.put(enumBoolean.getCode(), enumBoolean);
        }
    }

    @Getter
    private int code;

    EnumBoolean(int code) {
        this.code = code;
    }

    /**
     * convertor
     *
     * @param id
     * @return
     */
    public static EnumBoolean integer2Enum(final int id) {
        return ENUM_MAP.get(id);
    }
}
