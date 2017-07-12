package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.UnfrozenRecordDao;
import com.jkm.hss.account.entity.UnFrozenRecord;
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
     * @param unFrozenRecord
     */
    @Override
    public  void add(final UnFrozenRecord unFrozenRecord) {
        this.unfrozenRecordDao.insert(unFrozenRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param unFrozenRecord
     * @return
     */
    @Override
    public int update(final UnFrozenRecord unFrozenRecord) {
        return this.unfrozenRecordDao.update(unFrozenRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<UnFrozenRecord> getById(final long id) {
        return Optional.fromNullable(this.unfrozenRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param frozenRecordId
     * @return
     */
    @Override
    public Optional<UnFrozenRecord> getByFrozenRecordId(final long frozenRecordId) {
    return Optional.fromNullable(this.unfrozenRecordDao.selectByFrozenRecordId(frozenRecordId));
    }

    /**
     * {@inheritDoc}
     *
     * @param businessNo
     * @return
     */
    @Override
    public Optional<UnFrozenRecord> getByBusinessNo(final long businessNo) {
        return Optional.fromNullable(this.unfrozenRecordDao.selectByBusinessNo(businessNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @return
     */
    @Override
    public List<UnFrozenRecord> getByAccountId(final long accountId) {
        return this.unfrozenRecordDao.selectByAccountId(accountId);
    }
}
