package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.SplitAccountRecordDao;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class SplitAccountRecordServiceImpl implements SplitAccountRecordService {

    @Autowired
    private SplitAccountRecordDao splitAccountRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param record
     */
    @Override
    public void add(final SplitAccountRecord record) {
        this.splitAccountRecordDao.insert(record);
    }

    /**
     * {@inheritDoc}
     *
     * @param record
     * @return
     */
    @Override
    public int update(final SplitAccountRecord record) {
        return this.splitAccountRecordDao.update(record);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SplitAccountRecord> getById(final long id) {
        return Optional.fromNullable(this.splitAccountRecordDao.selectById(id));
    }
}
