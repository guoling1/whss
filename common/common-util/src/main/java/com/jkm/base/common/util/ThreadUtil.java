package com.jkm.base.common.util;

import com.google.common.base.Throwables;

/**
 * Created by hutao on 15/7/13.
 * 下午6:27
 */
public final class ThreadUtil {
    private ThreadUtil() {
    }

    /**
     * 线程睡眠
     * 转换InterruptedException异常为runtime异常
     *
     * @param millis
     */
    public static void sleep(final long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw Throwables.propagate(e);
        }
    }
}
