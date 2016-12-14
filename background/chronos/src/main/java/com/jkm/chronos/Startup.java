package com.jkm.chronos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by huangwei on 6/12/15.
 */
@Slf4j
public final class Startup {
    private Startup() {
    }

    /**
     * startup chronos
     *
     * @param args
     */
    public static void main(final String[] args) {
        final AbstractApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/chronos-task.xml");
        try {
            //可以获取通道配置，做简单测试
        } catch (final Throwable e) {
            log.error(e.getMessage(), e);
            throw e;
        }
        context.registerShutdownHook();
        log.info("chronos started!");
    }
}
