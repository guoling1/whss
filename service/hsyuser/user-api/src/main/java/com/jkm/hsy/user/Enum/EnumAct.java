package com.jkm.hsy.user.Enum;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by xingliujie on 2017/6/19.
 */
public enum EnumAct {
    /**
     * 收银家入网
     */
    SYJNET(1, "收银家入网"),
    /**
     * 收银家开通产品
     */
    SYJOPENPRODUCT(2, "收银家开通产品"),
    /**
     * 修改收银家入网
     */
    SYJUPDATENET(3, "修改收银家入网"),
    /**
     * 修改收银家产品
     */
    SYJUPDATEPRODUCT(4, "修改收银家产品"),
    /**
     * 厦门民生入网
     */
    XMMSNET(5, "厦门民生入网"),
    /**
     * 修改收银家产品
     */
    XMMSUPDATENET(6, "修改厦门民生入网");


    @Getter
    private int id;

    @Getter
    private String msg;
    EnumAct(int id, String msg) {
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
