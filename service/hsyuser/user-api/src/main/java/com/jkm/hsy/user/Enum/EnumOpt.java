package com.jkm.hsy.user.Enum;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by xingliujie on 2017/6/19.
 */
public enum EnumOpt {
    /**
     * 商户审核
     */
    MERCHANTAUDIT(1, "商户审核"),
    /**
     * 重新入网
     */
    REENTER(2, "重新入网"),
    /**
     * 修改费率
     */
    MODIFYRATES(3, "修改费率"),
    /**
     * 修改默认结算卡
     */
    MODIFYDEFAULTCARD(4, "修改默认结算卡");

    @Getter
    private int id;

    @Getter
    private String msg;
    EnumOpt(int id, String msg) {
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
