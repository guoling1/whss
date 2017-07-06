package com.jkm.base.common.util;

import lombok.AllArgsConstructor;

/**
 * Created by hutao on 16/3/3.
 * 下午3:46
 */
@AllArgsConstructor(staticName = "create")
public class CommonPreconditions {
    private final ExceptionFactory exceptionFactory;

    /**
     * 如果expression为false，抛出AccountException
     *
     * @param expression
     * @param errorMessage
     */
    public void assertThat(final boolean expression,
                           final String errorMessage) {
        if (!expression) {
            throw exceptionFactory.create(errorMessage);
        }
    }

    /**
     * 如果expression为false，抛出AccountException
     *
     * @param expression
     * @param errorMessage
     * @param errorMessageArgs
     */
    public void assertThat(final boolean expression,
                           final String errorMessage,
                           final Object... errorMessageArgs) {
        if (!expression) {
            throw exceptionFactory.create(StringFormatter.format(errorMessage, errorMessageArgs));
        }
    }

    /**
     * 检查是否为空
     *
     * @param reference
     * @param errorMessage
     * @param <T>
     * @return
     */
    public <T> T checkNotNull(final T reference,
                              final String errorMessage) {
        if (reference == null) {
            throw exceptionFactory.create(errorMessage);
        }
        return reference;
    }

    /**
     * 检查是否为空
     *
     * @param reference
     * @param errorMessage
     * @param <T>
     * @return
     */
    public <T> T checkNotNull(final T reference,
                              final String errorMessage,
                              final Object... errorMessageArgs) {
        if (reference == null) {
            throw exceptionFactory.create(StringFormatter.format(errorMessage, errorMessageArgs));
        }
        return reference;
    }

    /**
     * 异常工厂
     *
     * @param <T>
     */
    public interface ExceptionFactory<T extends RuntimeException> {
        /**
         * 创建
         *
         * @param msg
         * @return
         */
        T create(final String msg);
    }
}
