package com.jkm.hss.notifier.dao;

import com.jkm.hss.notifier.entity.ConsumeMsgFailRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
public interface ConsumeMsgFailRecordDao {

    /**
     * 插入
     *
     * @param consumeMsgFailRecord
     */
    void insert(ConsumeMsgFailRecord consumeMsgFailRecord);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 按消息tag查询待发送的消息记录
     *
     * @return
     */
    List<ConsumeMsgFailRecord> selectPendingRecordsByTag(@Param("tag") String tag);
}
