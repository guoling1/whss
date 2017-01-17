package com.jkm.hss.settle.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/1/12.
 *
 * 对账状态
 */
public enum EnumAccountCheckStatus {

    /**
     * 未对账
     */
    DUE_ACCOUNT_CHECK(1, "未对账"),

    /**
     * 对账完成无异常
     */
    SUCCESS_ACCOUNT_CHECK(2, "对账完成无异常"),

    /**
     * 有单边
     */
    SIDE_EXCEPTION(3, "有单边")
    ;



    @Getter
    private int id;

    @Getter
    private String value;

    EnumAccountCheckStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumAccountCheckStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumAccountCheckStatus> builder = new ImmutableMap.Builder<>();
        for (EnumAccountCheckStatus status : EnumAccountCheckStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumAccountCheckStatus of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
