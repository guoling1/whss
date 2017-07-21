package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2017-07-06.
 *
 * 结算通道
 */
public enum EnumSettleChannel {


    /**
     * 微信
     */
    WECHANT(1, "微信"),
    /**
     *支付宝
     */
    ALIPAY(2, "支付宝"),
    /**
     *  所有
     */
    ALL(0, "所有");

    @Getter
    private int id;
    @Getter
    private String str;

    EnumSettleChannel(int id, String str){
        this.id = id;
        this.str = str;
    }

    private static final ImmutableMap<Integer, EnumSettleChannel> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumSettleChannel> builder = new ImmutableMap.Builder<>();
        for (EnumSettleChannel type : EnumSettleChannel.values()) {
            builder.put(type.getId(), type);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSettleChannel of (final int id) {
        Preconditions.checkState(INIT_MAP.containsKey(id), "unknown status[{}]", id);
        return INIT_MAP.get(id);
    }
}
