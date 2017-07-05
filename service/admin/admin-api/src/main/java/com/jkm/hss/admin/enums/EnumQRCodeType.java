package com.jkm.hss.admin.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
public enum EnumQRCodeType {

    /**
     * 关注公众号
     */
    PUBLIC(1),
    /**
     * 扫静态码
     */
    SCAN_CODE(2);

    private static final ImmutableMap<Integer, EnumQRCodeType> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumQRCodeType> builder = new ImmutableMap.Builder<>();
        for (final EnumQRCodeType status : EnumQRCodeType.values()) {
            builder.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int code;

    EnumQRCodeType(final int code) {
        this.code = code;
    }

    /**
     * int convert to enum
     *
     * @param code int
     * @return enum
     */
    public static EnumQRCodeType ofEnum(final int code) {
        return STATUS_IMMUTABLE_MAP.get(code);
    }

}
