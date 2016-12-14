package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 支付通道唯一标识
 */
public enum EnumPayChannelSign {

    /****供应商以YG区分 100*****/
    /**
     * 微信扫码
     */
    YG_WEIXIN(101, "阳光微信扫码"),

    /**
     * 支付宝扫码
     */
    YG_ZHIFUBAO(102, "阳光支付宝扫码"),

    /**
     * 银联支付
     */
    YG_YINLIAN(103, "阳光银联支付");

    /**
     * id enum map
     */
    private static final ImmutableMap<String, EnumPayChannelSign> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<String, EnumPayChannelSign> builder = new ImmutableMap.Builder<>();
        for (final EnumPayChannelSign status : EnumPayChannelSign.values()) {
            builder.put(status.getName(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int id;
    @Getter
    private String name;

    EnumPayChannelSign(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * int convert to enum
     *
     * @param name String
     * @return enum
     */
    public static EnumPayChannelSign of(final String name) {
        return STATUS_IMMUTABLE_MAP.get(name);
    }
}
