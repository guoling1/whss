package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.bill.dao.SettlementRecordDao;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.service.SettlementRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2017/2/22.
 */
@Slf4j
@Service
public class SettlementRecordServiceImpl implements SettlementRecordService {

    @Autowired
    private SettlementRecordDao settlementRecordDao;
    /**
     * {@inheritDoc}
     *
     * @param settlementRecord
     * @return
     */
    @Override
    @Transactional
    public long add(final SettlementRecord settlementRecord) {
        this.settlementRecordDao.insert(settlementRecord);
        return settlementRecord.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param settlementRecord
     * @return
     */
    @Override
    @Transactional
    public int update(final SettlementRecord settlementRecord) {
        return this.settlementRecordDao.update(settlementRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     */
    @Override
    @Transactional
    public int updateStatus(final long id, final int status) {
        return this.settlementRecordDao.updateStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param settleStatus
     */
    @Override
    @Transactional
    public int updateSettleStatus(final long id, final int settleStatus) {
        return this.settlementRecordDao.updateSettleStatus(id, settleStatus);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SettlementRecord> getById(final long id) {
        return Optional.fromNullable(this.settlementRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SettlementRecord> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.settlementRecordDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param settleNo
     * @return
     */
    @Override
    public Optional<SettlementRecord> getBySettleNo(final String settleNo) {
        return Optional.fromNullable(this.settlementRecordDao.selectBySettleNo(settleNo));
    }
}
