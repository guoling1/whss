package com.jkm.base.common.databind;

/**
 * Created by huangwei
 */
public interface DataBind<T> {
    void put(T t);

    T get();

    void remove();
}
