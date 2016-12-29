package com.jkm.hss.account.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-09-27.
 */
public enum EnumFrozenStatus {

    FROZEN(1, "冻结中"),

    CONSUME(2, "已消费"),

    UN_FROZEN(3, "已解冻");
    @Getter
    private int id;
    @Getter
    private String value;

    EnumFrozenStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumFrozenStatus> INIT_ENUM_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumFrozenStatus> builder = new ImmutableMap.Builder<>();
        for (EnumFrozenStatus status : EnumFrozenStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_ENUM_MAP = builder.build();
    }


    public static EnumFrozenStatus of(final int status) {
        Preconditions.checkState(INIT_ENUM_MAP.containsKey(status),
                "unknown status[%s]", status);
        return INIT_ENUM_MAP.get(status);
    }
}
