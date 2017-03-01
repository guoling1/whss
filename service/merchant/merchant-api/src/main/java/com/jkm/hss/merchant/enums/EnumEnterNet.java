package com.jkm.hss.merchant.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 12:53
 */
public enum EnumEnterNet {
    /**
     * 1 不支持
     */
    UNSUPPORT(0, "不支持"),
    /**
     * 1 未入网
     */
    UNENT(1, "未入网"),
    /**
     * 2 已入网
     */
    HASENT(2, "已入网"),
    /**
     * 3 入网中
     */
    ENTING(3, "已入网"),
    /**
     * 4 入网失败
     */
    ENT_FAIL(4, "入网失败");

    @Getter
    private int id;

    @Getter
    private String msg;
    EnumEnterNet(int id, String msg) {
        this.msg = msg;
        this.id = id;
    }

    private static final ImmutableMap<Integer, EnumEnterNet> ID_INIT_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumEnterNet> builder = new ImmutableMap.Builder<>();
        for (final EnumEnterNet status : EnumEnterNet.values()) {
            builder.put(status.getId(), status);
        }
        ID_INIT_MAP = builder.build();
    }


    public static EnumEnterNet idOf(final int id) {
        return ID_INIT_MAP.get(id);
    }

}
