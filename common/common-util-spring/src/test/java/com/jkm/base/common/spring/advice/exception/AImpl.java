package com.jkm.base.common.spring.advice.exception;

import org.springframework.stereotype.Component;

/**
 * Created by hutao on 15/12/4.
 * 下午3:40
 */
@Component
public class AImpl implements A {
    /**
     * {@inheritDoc}
     */
    @Override
    public int func1() {
        throw new IllegalStateException("state exception");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int func2() {
        throw new IllegalArgumentException("argument exception");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int func3() {
        return 10;
    }
}
