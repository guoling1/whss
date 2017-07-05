package com.jkm.base.common.spring.advice.exception.convert;

import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/12/4.
 * 下午3:45
 */
@Component
public class ExceptionConverterForTest implements ExceptionConverter {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean needConvert(final Exception e) {
        return e instanceof IllegalStateException;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Exception convert(final Exception e) {
        return new RuntimeException("my error message");
    }
}
