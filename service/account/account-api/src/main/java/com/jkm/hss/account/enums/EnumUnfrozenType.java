package com.jkm.hss.account.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
public enum EnumUnfrozenType {

    /**
     * 解冻，返还冻结额度（第三方打款公司打款失败）
     */
    UNFROZEN(1, "解冻"),

    /**
     * 消费，（第三方打款公司打款成功）
     */
    CONSUME(2, "消费");
    @Getter
    private int id;
    @Getter
    private String value;

    EnumUnfrozenType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumUnfrozenType> INIT_ENUM_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumUnfrozenType> builder = new ImmutableMap.Builder<>();
        for (EnumUnfrozenType type : EnumUnfrozenType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_ENUM_MAP = builder.build();
    }


    public static EnumUnfrozenType of (final int type) {
        Preconditions.checkState(INIT_ENUM_MAP.containsKey(type),
                "unknown type[%s]", type);
        return INIT_ENUM_MAP.get(type);
    }
}
