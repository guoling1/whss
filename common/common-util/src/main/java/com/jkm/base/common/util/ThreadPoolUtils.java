package com.jkm.base.common.util;

import java.util.concurrent.*;

/**
 * Created by huangwei on 6/10/15.
 * 一个jvm内共用一个
 */
public final class ThreadPoolUtils {

    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(1, Runtime.getRuntime().availableProcessors(), 3, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(100), new ThreadPoolExecutor.CallerRunsPolicy());

    private ThreadPoolUtils() {
    }

    /**
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(final Callable<T> callable) {
        return EXECUTOR.submit(callable);
    }

    /**
     * @param runnable
     */
    public static void execute(final Runnable runnable) {
        EXECUTOR.execute(runnable);
    }
}
