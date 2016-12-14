package com.jkm.base.common.spring.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Collection;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/11/18.
 */
public class SpringContextHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    /**
     * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.
     */
    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 取得存储在静态变量中的ApplicationContext.
     */
    public static ApplicationContext getApplicationContext() {
        checkApplicationContext();
        return applicationContext;
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBeanByClazz(final Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBean(clazz);
    }

    /**
     * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.
     *
     * @param name
     * @param <T>
     * @return
     */
    public static <T> T getBeanByName(final String name) {
        checkApplicationContext();
        return (T) applicationContext.getBean(name);
    }

    /**
     * 按类型获得所有实现类
     *
     * @param clazz
     * @return
     */
    public static <T> Map<String, T> getBeansOfType(final Class<T> clazz) {
        checkApplicationContext();
        return applicationContext.getBeansOfType(clazz);
    }

    /**
     * 按类型获得一个实现类，没有时返回null
     *
     * @param clazz
     * @return
     */
    public static <T> T getBeanOneOfType(final Class<T> clazz) {
        checkApplicationContext();
        Collection<T> results = applicationContext.getBeansOfType(clazz).values();
        return results.size() > 0 ? results.iterator().next() : null;
    }

    private static void checkApplicationContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("未注入SpringContextHolder");
        }
    }
}
