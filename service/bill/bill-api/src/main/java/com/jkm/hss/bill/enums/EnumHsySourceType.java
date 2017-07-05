package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by wayne on 17/5/17.
 */
public enum EnumHsySourceType {

    QRCODE(1,"二维码付款"),
    SCAN(2,"主扫付款"),
    ;
    @Getter
    private int id;
    @Getter
    private String value;

    EnumHsySourceType(final int id, final String value) {
        this.id = id;
        this.value = value;
    }
    private static final ImmutableMap<Integer, EnumHsySourceType> INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumHsySourceType> builder = new ImmutableMap.Builder<>();
        for (EnumHsySourceType status : EnumHsySourceType.values()) {
            builder.put(status.getId(), status);
        }
        INIT_MAP = builder.build();
    }

    public static EnumHsySourceType of (final int status) {
        Preconditions.checkState(INIT_MAP.containsKey(status), "unknown status[{}]", status);
        return INIT_MAP.get(status);
    }
}
