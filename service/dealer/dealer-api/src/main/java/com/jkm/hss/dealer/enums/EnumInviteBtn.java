package com.jkm.hss.dealer.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by xingliujie on 2017/2/10.
 */
public enum EnumInviteBtn {
    /**
     * 关
     */
    OFF(1),

    /**
     * 开
     */
    ON(2);

    /**
     * id enum map
     */
    private static final ImmutableMap<Integer, EnumDealerLevel> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<Integer, EnumDealerLevel> builder = new ImmutableMap.Builder<>();
        for (final EnumDealerLevel status : EnumDealerLevel.values()) {
            builder.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int id;

    EnumInviteBtn(final int id) {
        this.id = id;
    }
}
