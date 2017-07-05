package com.jkm.hss.dealer.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/11/22.
 *
 * 经销商状态
 */
public enum EnumDealerStatus {

    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 禁用
     */
    DISABLE(2);
    /**
     * id enum map
     */
    private static final ImmutableMap<Integer, EnumDealerStatus> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumDealerStatus> builder = new ImmutableMap.Builder<>();
        for (final EnumDealerStatus status : EnumDealerStatus.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int id;

    EnumDealerStatus(final int id) {
        this.id = id;
    }

    /**
     * int convert to enum
     *
     * @param id int
     * @return enum
     */
    public static EnumDealerStatus of(final int id) {
        return STATUS_IMMUTABLE_MAP.get(id);
    }

    /**
     * 判断状态是否合法
     *
     * @param id
     * @return
     */
    public static boolean isAdminUserStatus(final int id) {
        return STATUS_IMMUTABLE_MAP.containsKey(id);
    }
}
