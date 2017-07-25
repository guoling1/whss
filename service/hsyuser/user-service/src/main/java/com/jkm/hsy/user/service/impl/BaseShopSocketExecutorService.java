package com.jkm.hsy.user.service.impl;


/**
 * Created by yulong.zhang on 2017/7/24.
 */
public interface BaseShopSocketExecutorService {

    /**
     * 运行任务
     *
     * @param shopId
     * @param msg
     */
    void runTask(long shopId, String msg);
}
