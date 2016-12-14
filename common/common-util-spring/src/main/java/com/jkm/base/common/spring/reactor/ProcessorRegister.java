package com.jkm.base.common.spring.reactor;

/**
 * Created by hutao on 15/7/11.
 * 下午3:59
 */
public interface ProcessorRegister {
    /**
     * 向reactor注册处理器
     *
     * @param taskReactor
     */
    void register(final TaskReactor taskReactor);
}
