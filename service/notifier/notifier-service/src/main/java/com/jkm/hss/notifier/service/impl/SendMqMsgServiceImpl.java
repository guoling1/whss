package com.jkm.hss.notifier.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.notifier.dao.ConsumeMsgSplitProfitRecordDao;
import com.jkm.hss.notifier.entity.ConsumeMsgSplitProfitRecord;
import com.jkm.hss.notifier.service.SendMqMsgService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
@Slf4j
@Service
public class SendMqMsgServiceImpl implements SendMqMsgService {

    @Autowired
    private ConsumeMsgSplitProfitRecordDao consumeMsgSplitProfitRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param consumeMsgSplitProfitRecord
     */
    @Override
    public long add(final ConsumeMsgSplitProfitRecord consumeMsgSplitProfitRecord) {
        this.consumeMsgSplitProfitRecordDao.insert(consumeMsgSplitProfitRecord);
        return consumeMsgSplitProfitRecord.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateStatus(final long id, final int status) {
        return this.consumeMsgSplitProfitRecordDao.updateStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ConsumeMsgSplitProfitRecord> getById(final long id) {
        return Optional.fromNullable(this.consumeMsgSplitProfitRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ConsumeMsgSplitProfitRecord> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.consumeMsgSplitProfitRecordDao.selectByIdWithLock(id));
    }


    /**
     * {@inheritDoc}
     *
     * @param tag
     * @return
     */
    @Override
    public List<ConsumeMsgSplitProfitRecord> getPendingRecordsByTag(final String tag) {
        return this.consumeMsgSplitProfitRecordDao.selectPendingRecordsByTag(tag);
    }
}
