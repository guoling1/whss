package com.jkm.hsy.user.Enum;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumNetStatus {
    /**
     * 1 需入网，未入网
     */
    UNSUPPORT(1, "需入网，未入网"),
    /**
     * 2 不需入网
     */
    UNENT(2, "不需入网"),
    /**
     * 3 已提交
     */
    HASENT(3, "已提交"),
    /**
     * 4 成功
     */
    ENTING(4, "成功"),
    /**
     * 5 失败
     */
    ENT_FAIL(5, "失败");

    @Getter
    private int id;

    @Getter
    private String msg;
    EnumNetStatus(int id, String msg) {
        this.msg = msg;
        this.id = id;
    }

    private static final ImmutableMap<Integer, EnumNetStatus> ID_INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumNetStatus> builder = new ImmutableMap.Builder<>();
        for (final EnumNetStatus status : EnumNetStatus.values()) {
            builder.put(status.getId(), status);
        }
        ID_INIT_MAP = builder.build();
    }


    public static EnumNetStatus idOf(final int id) {
        return ID_INIT_MAP.get(id);
    }

}
