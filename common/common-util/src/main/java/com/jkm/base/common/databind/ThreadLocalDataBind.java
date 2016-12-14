package com.jkm.base.common.databind;


/**
 * Created by huangwei
 */
public class ThreadLocalDataBind<T> implements DataBind<T> {
    private final ThreadLocal<T> threadLocal = new ThreadLocal<T>();

    @Override
    public void put(T t) {
        threadLocal.set(t);
    }

    @Override
    public T get() {
        return threadLocal.get();
    }

    @Override
    public void remove() {
        threadLocal.remove();
    }
}
