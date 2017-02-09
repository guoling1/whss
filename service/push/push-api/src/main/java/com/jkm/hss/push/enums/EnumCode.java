package com.jkm.hss.push.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
public enum EnumCode {

    CODESUCCESS(100),//审核成功

    CODEFAIL(101);//审核失败


    /**
     * id enum map
     */
    private static final ImmutableMap<Integer, EnumCode> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumCode> builder = new ImmutableMap.Builder<>();
        for (final EnumCode status : EnumCode.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }
    @Getter
    private int id;

    EnumCode(final int id) {
        this.id = id;
    }

    /**
     * int convert to enum
     *
     * @param id int
     * @return enum
     */
    public static EnumCode of(final int id) {
        return STATUS_IMMUTABLE_MAP.get(id);
    }
}
