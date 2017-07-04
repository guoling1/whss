package com.jkm.base.common.spring.advice.profile.method;

import java.lang.reflect.Method;

/**
 * Created by hutao on 15/9/9.
 * 下午2:58
 */
public class NullProfileAdvice implements ProfileAdvice {
    /**
     * {@inheritDoc}
     */
    @Override
    public void pre(final Class clazz, final Method method) {
        //DO nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void post(final Class clazz, final Method method) {
        //DO nothing
    }
}
