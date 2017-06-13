package com.jkm.hss.notifier.service;

import com.google.common.base.Optional;
import com.jkm.hss.notifier.entity.ConsumeMsgSplitProfitRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
public interface SendMqMsgService {

    /**
     * 插入
     *
     * @param consumeMsgSplitProfitRecord
     */
    long add(ConsumeMsgSplitProfitRecord consumeMsgSplitProfitRecord);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(long id, int status);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<ConsumeMsgSplitProfitRecord> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<ConsumeMsgSplitProfitRecord> getByIdWithLock(long id);

    /**
     * 按消息tag查询待发送的消息记录
     *
     * @return
     */
    List<ConsumeMsgSplitProfitRecord> getPendingRecordsByTag(String tag);
}
