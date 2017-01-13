package com.jkm.hss.settle.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/1/13.
 *
 * 结算 选择 枚举
 */
public enum  EnumSettleOptionType {

    /**
     * 正常结算
     */
    SETTLE_NORMAL(1, "正常结算"),

    /**
     * 结算已对账部分
     */
    SETTLE_ACCOUNT_CHECKED(2, "结算已对账部分"),

    /**
     * 强制结算全部
     */
    SETTLE_ALL(3, "强制结算全部")
    ;



    @Getter
    private int id;

    @Getter
    private String value;

    EnumSettleOptionType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumSettleOptionType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumSettleOptionType> builder = new ImmutableMap.Builder<>();
        for (EnumSettleOptionType type : EnumSettleOptionType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSettleOptionType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown type[{}]", type);
        return INIT_MAP.get(type);
    }
}
