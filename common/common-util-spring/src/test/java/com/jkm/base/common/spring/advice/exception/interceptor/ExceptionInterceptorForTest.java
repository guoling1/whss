package com.jkm.base.common.spring.advice.exception.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/12/4.
 * 下午3:47
 */
@Component
public class ExceptionInterceptorForTest implements ExceptionInterceptor {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needHandle(final Exception e) {
        return e instanceof IllegalStateException;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handleException(final ProceedingJoinPoint pjp, final Exception e) {
        return 11;
    }
}
