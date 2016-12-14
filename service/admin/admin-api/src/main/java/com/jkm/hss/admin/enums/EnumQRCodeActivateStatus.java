package com.jkm.hss.admin.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
public enum EnumQRCodeActivateStatus {

    /**
     * 未激活
     */
    UN_ACTIVATE(1),

    /**
     * 激活
     */
    ACTIVATE(2);

    private static final ImmutableMap<Integer, EnumQRCodeActivateStatus> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumQRCodeActivateStatus> builder = new ImmutableMap.Builder<>();
        for (final EnumQRCodeActivateStatus status : EnumQRCodeActivateStatus.values()) {
            builder.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int code;

    EnumQRCodeActivateStatus(final int code) {
        this.code = code;
    }

    /**
     * int convert to enum
     *
     * @param code int
     * @return enum
     */
    public static EnumQRCodeActivateStatus of(final int code) {
        return STATUS_IMMUTABLE_MAP.get(code);
    }
}
