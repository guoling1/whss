package com.jkm.hsy.user.Enum;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * 政策模板
 */
public enum EnumPolicyType {

    WECHAT("wechat", "微信"),
    ALIPAY("alipay", "支付宝"),
    WITHDRAW("withdraw", "提现手续费");

    /**
     * id enum map
     */
    private static final ImmutableMap<String, EnumPolicyType> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<String, EnumPolicyType> builder = new ImmutableMap.Builder<>();
        for (final EnumPolicyType status : EnumPolicyType.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private String id;
    @Getter
    private String name;

    EnumPolicyType(final String id, final String name) {
        this.id = id;
        this.name = name;
    }
    /**
     * int convert to enum
     *
     * @param
     * @return enum
     */
    public static EnumPolicyType of(final String type) {
        return STATUS_IMMUTABLE_MAP.get(type);
    }

}
