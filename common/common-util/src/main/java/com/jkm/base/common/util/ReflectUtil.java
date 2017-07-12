package com.jkm.base.common.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by huangwei on 6/6/15.
 */
public final class ReflectUtil {
    private ReflectUtil() {
    }

    /**
     * 获取父类的泛型参数
     *
     * @param clazz
     * @param index
     * @return
     */
    public static Class<Object> getSuperClassGenricType(final Class clazz, final int index) {
        final Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        final Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }

        return (Class) params[index];
    }
}
