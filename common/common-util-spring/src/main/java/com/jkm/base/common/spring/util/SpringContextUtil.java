package com.jkm.base.common.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by hutao on 15/6/26.
 * 下午4:26
 */
@Service
public final class SpringContextUtil implements ApplicationContextAware {
    private static ApplicationContext context;

    /**
     * 返回spring上下文
     *
     * @return spring上下文
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * get bean object
     *
     * @return bean object
     */
    public static <T> T getBean(final String beanName, final Class<T> tClass) {
        return getContext().getBean(beanName, tClass);
    }

    /**
     * 设置spring上下午
     *
     * @param context spring上下文
     * @throws BeansException
     */
    @SuppressWarnings("static-access")
    public void setApplicationContext(final ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
