package com.jkm.hss.account.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/1/13.
 *
 * 账户所属用户类型
 */
public enum EnumAccountUserType {


    COMPANY(1, "金开门"),

    MERCHANT(2, "商户"),

    DEALER(3, "代理商"),

    BRANCH_COMPANY(4, "金开门");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumAccountUserType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumAccountUserType> INIT_ENUM_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumAccountUserType> builder = new ImmutableMap.Builder<>();
        for (EnumAccountUserType status : EnumAccountUserType.values()) {
            builder.put(status.getId(), status);
        }
        INIT_ENUM_MAP = builder.build();
    }


    public static EnumAccountUserType of(final int status) {
        Preconditions.checkState(INIT_ENUM_MAP.containsKey(status),
                "unknown status[%s]", status);
        return INIT_ENUM_MAP.get(status);
    }
}
