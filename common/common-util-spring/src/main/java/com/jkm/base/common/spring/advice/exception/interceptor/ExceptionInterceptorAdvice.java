package com.jkm.base.common.spring.advice.exception.interceptor;

import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by hutao on 15/12/4.
 * 下午3:13
 */
public class ExceptionInterceptorAdvice {
    @Setter
    private ExceptionInterceptor exceptionInterceptor;

    /**
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception e) {
            if (exceptionInterceptor.needHandle(e)) {
                return exceptionInterceptor.handleException(pjp, e);
            }
            throw e;
        }
    }
}
