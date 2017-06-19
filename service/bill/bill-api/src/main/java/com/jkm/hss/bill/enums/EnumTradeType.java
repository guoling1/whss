package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 交易类型
 */
public enum EnumTradeType {

    /**
     * 支付
     */
    PAY(1, "支付"),

    /**
     * 充值
     */
    RECHARGE(2, "充值"),

    /**
     * 提现
     */
    WITHDRAW(3, "提现");


    @Getter
    private int id;

    @Getter
    private String value;

    EnumTradeType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static ImmutableMap<Integer, EnumTradeType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumTradeType> builder = new ImmutableMap.Builder<>();
        for (EnumTradeType tradeType : EnumTradeType.values()) {
            builder.put(tradeType.getId(), tradeType);
        }
        INIT_MAP = builder.build();
    }

    public static EnumTradeType of (final int tradeType) {
        Preconditions.checkState(INIT_MAP.containsKey(tradeType), "unknown tradeType[{}]", tradeType);
        return INIT_MAP.get(tradeType);
    }
}
