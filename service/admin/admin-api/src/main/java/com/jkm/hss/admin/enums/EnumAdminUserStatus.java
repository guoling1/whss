package com.jkm.hss.admin.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by huangwei on 7/23/15.
 */
public enum EnumAdminUserStatus {
    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 禁用
     */
    DISABLE(2);

    private static final ImmutableMap<Integer, EnumAdminUserStatus> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumAdminUserStatus> builder = new ImmutableMap.Builder<>();
        for (final EnumAdminUserStatus status : EnumAdminUserStatus.values()) {
            builder.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int code;

    EnumAdminUserStatus(final int code) {
        this.code = code;
    }

    /**
     * int convert to enum
     *
     * @param code int
     * @return enum
     */
    public static EnumAdminUserStatus ofEnum(final int code) {
        return STATUS_IMMUTABLE_MAP.get(code);
    }

}
