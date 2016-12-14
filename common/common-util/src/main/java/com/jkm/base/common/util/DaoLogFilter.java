package com.jkm.base.common.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;

/**
 * Created by hutao on 15/7/10.
 * 上午11:11
 */
public class DaoLogFilter extends Filter<ILoggingEvent> {
    /**
     * 过滤掉与dao相关的日志
     *
     * @param event
     * @return
     */
    @Override
    public FilterReply decide(final ILoggingEvent event) {
        if (event.getLoggerName().contains("dao")) {
            return FilterReply.DENY;
        } else {
            return FilterReply.NEUTRAL;
        }
    }
}