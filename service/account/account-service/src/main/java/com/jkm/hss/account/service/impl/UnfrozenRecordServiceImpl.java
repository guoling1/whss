package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.UnfrozenRecordDao;
import com.jkm.hss.account.entity.UnfrozenRecord;
import com.jkm.hss.account.sevice.UnfrozenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
@Slf4j
@Service
public class UnfrozenRecordServiceImpl implements UnfrozenRecordService {

    @Autowired
    private UnfrozenRecordDao unfrozenRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param unfrozenRecord
     */
    @Override
    public  void add(final UnfrozenRecord unfrozenRecord) {
        this.unfrozenRecordDao.insert(unfrozenRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param unfrozenRecord
     * @return
     */
    @Override
    public int update(final UnfrozenRecord unfrozenRecord) {
        return this.unfrozenRecordDao.update(unfrozenRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<UnfrozenRecord> getById(final long id) {
        return Optional.fromNullable(this.unfrozenRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param frozenRecordId
     * @return
     */
    @Override
    public Optional<UnfrozenRecord> getByFrozenRecordId(final long frozenRecordId) {
    return Optional.fromNullable(this.unfrozenRecordDao.selectByFrozenRecordId(frozenRecordId));
    }

    /**
     * {@inheritDoc}
     *
     * @param businessNo
     * @return
     */
    @Override
    public Optional<UnfrozenRecord> getByBusinessNo(final long businessNo) {
        return Optional.fromNullable(this.unfrozenRecordDao.selectByBusinessNo(businessNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @return
     */
    @Override
    public List<UnfrozenRecord> getByAccountId(final long accountId) {
        return this.unfrozenRecordDao.selectByAccountId(accountId);
    }
}
