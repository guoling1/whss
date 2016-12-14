package com.jkm.base.common.util;

import java.util.UUID;

/**
 * Created by hutao on 15/6/12.
 */
public final class UUIDGenerator {
    private UUIDGenerator() {
    }

    /**
     * 生成随即字符串
     *
     * @return
     */
    public static String generate() {
        return UUID.randomUUID().toString();
    }
}
