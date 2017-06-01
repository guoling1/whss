package com.jkm.hss.notifier.service;

import com.jkm.hss.notifier.entity.ConsumeMsgFailRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
public interface SendMqMsgService {

    /**
     * 插入
     *
     * @param consumeMsgFailRecord
     */
    long add(ConsumeMsgFailRecord consumeMsgFailRecord);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(long id, int status);

    /**
     * 按消息tag查询待发送的消息记录
     *
     * @return
     */
    List<ConsumeMsgFailRecord> getPendingRecordsByTag(String tag);
}
