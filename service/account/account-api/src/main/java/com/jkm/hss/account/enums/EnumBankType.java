package com.jkm.hss.account.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/4/14.
 *
 * 银行卡类型枚举
 */
public enum EnumBankType {


    DEBIT_CARD(0, "借记卡"),

    CREDIT_CARD(1, "贷记卡");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumBankType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumBankType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumBankType> builder = new ImmutableMap.Builder<>();
        for (EnumBankType type : EnumBankType.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumBankType of (final int type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown type[{}]", type);
        return INIT_MAP.get(type);
    }
}
