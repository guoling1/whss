package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by wayne on 17/5/17.
 */
public enum EnumHsyOrderStatus {
    /**
     * 待支付
     */
    DUE_PAY(1, "待支付"),
    PAY_SUCCESS(2, "收款成功"),
    PAY_FAIL(3, "支付失败"),
    REFUNDING(4,"退款中"),
    REFUND_PART(5,"部分退款"),
    REFUND_SUCCESS(6, "全部退款"),
    REFUND_FAIL(7, "退款失败")
    ;

    @Getter
    private int id;
    @Getter
    private String value;

    EnumHsyOrderStatus(final int id, final String value) {
        this.id = id;
        this.value = value;
    }


    private static final ImmutableMap<Integer, EnumHsyOrderStatus> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumHsyOrderStatus> builder = new ImmutableMap.Builder<>();
        for (EnumHsyOrderStatus status : EnumHsyOrderStatus.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumHsyOrderStatus of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }

}
