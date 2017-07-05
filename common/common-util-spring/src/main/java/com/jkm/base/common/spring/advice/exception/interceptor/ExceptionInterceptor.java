package com.jkm.base.common.spring.advice.exception.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by hutao on 15/12/4.
 * 下午3:13
 */
public interface ExceptionInterceptor {
    /**
     * 是否需要处理此异常
     *
     * @param e
     * @return
     */
    boolean needHandle(final Exception e);

    /**
     * 处理异常
     *
     * @param pjp
     * @param e
     * @return
     */
    Object handleException(final ProceedingJoinPoint pjp, final Exception e);
}
