package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2017/2/23.
 */
public enum EnumSettlementRecordStatus {

    /**
     * 待提现
     */
    WAIT_WITHDRAW(1, "待提现"),

    /**
     * 提现中
     */
    WITHDRAWING(2, "提现中"),

    /**
     * 提现成功
     */
    WITHDRAW_SUCCESS(3, "提现成功"),

    /**
     * 提现失败
     */
    WITHDRAW_FAIL(4, "提现失败")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumSettlementRecordStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }

    private static final ImmutableMap<Integer, EnumSettlementRecordStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumSettlementRecordStatus> builder = new ImmutableMap.Builder<>();
        for (EnumSettlementRecordStatus status : EnumSettlementRecordStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumSettlementRecordStatus of (final int id) {
        Preconditions.checkState(INIT_MAP.containsKey(id));
        return INIT_MAP.get(id);
    }
}
