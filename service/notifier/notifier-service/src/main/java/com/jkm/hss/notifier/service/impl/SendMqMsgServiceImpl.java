package com.jkm.hss.notifier.service.impl;

import com.jkm.hss.notifier.dao.ConsumeMsgFailRecordDao;
import com.jkm.hss.notifier.entity.ConsumeMsgFailRecord;
import com.jkm.hss.notifier.service.SendMqMsgService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/6/1.
 */
@Data
@Service
public class SendMqMsgServiceImpl implements SendMqMsgService {

    @Autowired
    private ConsumeMsgFailRecordDao consumeMsgFailRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param consumeMsgFailRecord
     */
    @Override
    public long add(final ConsumeMsgFailRecord consumeMsgFailRecord) {
        this.consumeMsgFailRecordDao.insert(consumeMsgFailRecord);
        return consumeMsgFailRecord.getId();
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
        return this.consumeMsgFailRecordDao.updateStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param tag
     * @return
     */
    @Override
    public List<ConsumeMsgFailRecord> getPendingRecordsByTag(final String tag) {
        return this.consumeMsgFailRecordDao.selectPendingRecordsByTag(tag);
    }
}
