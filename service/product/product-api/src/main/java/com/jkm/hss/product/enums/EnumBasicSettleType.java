package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2017-02-24.
 */
public enum EnumBasicSettleType {

    AUTO_PLAY("AUTO", "通道自动打款结算"),

    SELF_PLAY("SELF", "自主打款结算");

    @Getter
    private String id;

    @Getter
    private String msg;

    EnumBasicSettleType(String id, String msg){
        this.id = id;
        this.msg = msg;
    }

    public static final ImmutableMap<String , EnumBasicSettleType> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<String, EnumBasicSettleType> builder = new ImmutableMap.Builder<>();
        for (final EnumBasicSettleType status : EnumBasicSettleType.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    /**
     * int convert to enum
     *
     * @param
     * @return enum
     */
    public static EnumBasicSettleType of(final String type) {
        return STATUS_IMMUTABLE_MAP.get(type);
    }
}
