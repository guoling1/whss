package com.jkm.hss.admin.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
public enum EnumQRCodeDistributeType2 {

    /**
     * boss后台
     */
    ADMIN(1),
    /**
     * 代理商后台
     */
    DEALER(2),
    /**
     * 分公司
     */
    OEM(3);

    private static final ImmutableMap<Integer, EnumQRCodeDistributeType2> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumQRCodeDistributeType2> builder = new ImmutableMap.Builder<>();
        for (final EnumQRCodeDistributeType2 status : EnumQRCodeDistributeType2.values()) {
            builder.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int code;

    EnumQRCodeDistributeType2(final int code) {
        this.code = code;
    }

    /**
     * int convert to enum
     *
     * @param code int
     * @return enum
     */
    public static EnumQRCodeDistributeType2 ofEnum(final int code) {
        return STATUS_IMMUTABLE_MAP.get(code);
    }

}
