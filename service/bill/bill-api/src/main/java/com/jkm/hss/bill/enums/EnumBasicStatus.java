package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/23.
 *
 * 支付结果
 */
public enum EnumBasicStatus {


    SUCCESS(1, "成功"),

    FAIL(2, "失败"),

    HANDLING(3, "处理中");

    @Getter
    private int id;
    @Getter
    private String value;

    EnumBasicStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }



    private static final ImmutableMap<Integer, EnumBasicStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumBasicStatus> builder = new ImmutableMap.Builder<>();
        for (EnumBasicStatus status : EnumBasicStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumBasicStatus of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
