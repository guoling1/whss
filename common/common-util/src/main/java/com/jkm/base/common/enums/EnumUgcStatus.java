package com.jkm.base.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangwei on 5/5/16.
 */
public enum EnumUgcStatus {
    /**
     * 未审核
     */
    NOT_AUDIT(1),

    /**
     * 审核通过
     */
    PASS(2),

    /**
     * 审核不通过
     */
    NOT_PASS(3),

    /**
     * 用户删除
     */
    USER_DELETE(4);

    private static final Map<Integer, EnumUgcStatus> ENUM_MAP = new HashMap<>();

    static {
        for (final EnumUgcStatus enumUgcStatus : EnumUgcStatus.values()) {
            ENUM_MAP.put(enumUgcStatus.getCode(), enumUgcStatus);
        }
    }

    @Getter
    private int code;

    EnumUgcStatus(int code) {
        this.code = code;
    }

    /**
     * convertor
     *
     * @param id
     * @return
     */
    public static EnumUgcStatus integer2Enum(final int id) {
        return ENUM_MAP.get(id);
    }

    /**
     * 是否可用
     *
     * @return
     */
    public boolean isAvailable() {
        return this == NOT_AUDIT || this == PASS;
    }

}
