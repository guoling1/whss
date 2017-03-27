package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 打款单状态
 */
public enum EnumPlayStatus {


    /**
     * 待打款
     */
    DUE_PLAY_MONEY(1, "待打款"),

    /**
     * 打款中
     */
//    PLAY_MONEY_ING(2, "打款中"),

    /**
     * 准备请求(打款中 1)
     */
    PREPARE_REQUEST(2, "准备请求"),

    /**
     * 请求成功 (打款中 2)
     */
    REQUEST_SUCCESS(3, "请求成功"),

    /**
     * 打款成功
     */
    PLAY_MONEY_SUCCESS(4, "打款成功"),

    /**
     * 打款失败
     */
    PLAY_MONEY_FAIL(5, "打款失败")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumPlayStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }


    private static final ImmutableMap<Integer, EnumPlayStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumPlayStatus> builder = new ImmutableMap.Builder<>();
        for (EnumPlayStatus status : EnumPlayStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumPlayStatus of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
