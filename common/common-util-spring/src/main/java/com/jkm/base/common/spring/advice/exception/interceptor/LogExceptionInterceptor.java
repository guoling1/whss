package com.jkm.base.common.spring.advice.exception.interceptor;

import com.google.common.base.Throwables;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/12/4.
 * 下午4:07
 * 只记录日志，不拦截异常
 */
@Component
@Slf4j
public class LogExceptionInterceptor implements ExceptionInterceptor {
    @Setter
    private boolean open = true;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needHandle(final Exception e) {
        return open;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handleException(final ProceedingJoinPoint pjp,
                                  final Exception e) {
        log.error("log interceptor catch exception at [{}]", pjp.toString()
                + ",error:" + e.getMessage(), e);
        throw Throwables.propagate(e);
    }
}
