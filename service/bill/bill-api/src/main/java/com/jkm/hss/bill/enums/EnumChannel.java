package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 上游通道方
 */
public enum EnumChannel {

    /**
     * 扫米
     */
    SAOMI(1, "扫米"),

    /**
     * 摩宝
     */
    MOBAO(2, "摩宝"),

    /**
     * 卡盟
     */
    KAMENG(3, "卡盟"),

    /**
     * 合众易宝
     */
    HEZONG_YIBAO(4, "合众易宝"),

    /**
     * 溢+
     */
    YIJIA(5, "溢+")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumChannel(final int id, final String value) {
        this.id = id;
        this.value = value;
    }


    private static final ImmutableMap<Integer, EnumChannel> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumChannel> builder = new ImmutableMap.Builder<>();
        for (EnumChannel status : EnumChannel.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumChannel of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
