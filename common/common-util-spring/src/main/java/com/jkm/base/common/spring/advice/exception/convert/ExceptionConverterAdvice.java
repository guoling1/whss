package com.jkm.base.common.spring.advice.exception.convert;

import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by hutao on 15/12/4.
 * 下午3:06
 */
public class ExceptionConverterAdvice {
    @Setter
    private ExceptionConverter exceptionConverter;

    /**
     * @param pjp
     * @return
     * @throws Throwable
     */
    public Object around(final ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed();
        } catch (Exception e) {
            if (exceptionConverter.needConvert(e)) {
                throw exceptionConverter.convert(e);
            }
            throw e;
        }
    }
}
