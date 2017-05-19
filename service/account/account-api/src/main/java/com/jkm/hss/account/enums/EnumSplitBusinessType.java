package com.jkm.hss.account.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2017-02-13.
 */
public enum EnumSplitBusinessType {

    HSSPAY("hssPay", "好收收- 收款"),

    HSSWITHDRAW("hssWithdraw", "好收收-提现"),

    HSSPROMOTE("hssUpgrade", "好收收-升级费"),

    HSYPAY("hsyPay", "好收银-收款");
    @Getter
    private String id;
    @Getter
    private String value;

    EnumSplitBusinessType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }


    private static final ImmutableMap<String, EnumSplitBusinessType> INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumSplitBusinessType> builder = new ImmutableMap.Builder<>();
        for (EnumSplitBusinessType businessType : EnumSplitBusinessType.values()) {
            builder.put(businessType.getId(), businessType);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSplitBusinessType of (final String businessType) {
        Preconditions.checkState(INIT_MAP.containsKey(businessType), "unknown businessType[{}]", businessType);
        return INIT_MAP.get(businessType);
    }
}
