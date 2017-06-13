package com.jkm.hss.notifier.dao;

import com.jkm.hss.notifier.entity.ConsumeMsgSplitProfitRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
@Repository
public interface ConsumeMsgSplitProfitRecordDao {

    /**
     * 插入
     *
     * @param consumeMsgSplitProfitRecord
     */
    void insert(ConsumeMsgSplitProfitRecord consumeMsgSplitProfitRecord);

    /**
     * 更新状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    ConsumeMsgSplitProfitRecord selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    ConsumeMsgSplitProfitRecord selectByIdWithLock(@Param("id") long id);

    /**
     * 按消息tag查询待发送的消息记录
     *
     * @return
     */
    List<ConsumeMsgSplitProfitRecord> selectPendingRecordsByTag(@Param("tag") String tag);
}
