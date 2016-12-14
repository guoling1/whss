package com.jkm.hss.admin.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/25.
 */
public enum EnumQRCodeDistributionStatus {
    /**
     * 未分配
     */
    UN_DISTRIBUTION(1),

    /**
     * 已分配
     */
    DISTRIBUTION(2);

    private static final ImmutableMap<Integer, EnumQRCodeDistributionStatus> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumQRCodeDistributionStatus> builder = new ImmutableMap.Builder<>();
        for (final EnumQRCodeDistributionStatus status : EnumQRCodeDistributionStatus.values()) {
            builder.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int code;

    EnumQRCodeDistributionStatus(final int code) {
        this.code = code;
    }

    /**
     * int convert to enum
     *
     * @param code int
     * @return enum
     */
    public static EnumQRCodeDistributionStatus of(final int code) {
        return STATUS_IMMUTABLE_MAP.get(code);
    }
}
