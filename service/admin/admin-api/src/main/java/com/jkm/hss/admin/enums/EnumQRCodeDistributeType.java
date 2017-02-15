package com.jkm.hss.admin.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
public enum EnumQRCodeDistributeType {

    /**
     * 实体码
     */
    ENTITYCODE(1),
    /**
     * 电子码
     */
    ELECTRONICCODE(2);

    private static final ImmutableMap<Integer, EnumQRCodeDistributeType> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumQRCodeDistributeType> builder = new ImmutableMap.Builder<>();
        for (final EnumQRCodeDistributeType status : EnumQRCodeDistributeType.values()) {
            builder.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int code;

    EnumQRCodeDistributeType(final int code) {
        this.code = code;
    }

    /**
     * int convert to enum
     *
     * @param code int
     * @return enum
     */
    public static EnumQRCodeDistributeType ofEnum(final int code) {
        return STATUS_IMMUTABLE_MAP.get(code);
    }

}
