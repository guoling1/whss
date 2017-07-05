package com.jkm.base.common.spring.advice.profile.method;

import java.lang.reflect.Method;

/**
 * Created by hutao on 15/9/9.
 * 下午2:37
 */
public interface ProfileAdvice {
    /**
     * 函数开始前
     *
     * @param clazz
     * @param method
     */
    void pre(final Class clazz, final Method method);

    /**
     * 函数执行结束
     *
     * @param clazz
     * @param method
     */
    void post(final Class clazz, final Method method);
}
