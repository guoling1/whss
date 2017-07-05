package com.jkm.base.common.spring.advice.exception.convert;

/**
 * Created by hutao on 15/12/4.
 * 下午3:07
 */
public interface ExceptionConverter {
    /**
     * 是否需要转换
     *
     * @param e
     * @return
     */
    boolean needConvert(final Exception e);

    /**
     * 转换异常
     *
     * @param e
     * @return
     */
    Exception convert(final Exception e);
}
