package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.FrozenRecordDao;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.enums.EnumFrozenStatus;
import com.jkm.hss.account.sevice.FrozenRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
@Slf4j
@Service
public class FrozenRecordServiceImpl implements FrozenRecordService {

    @Autowired
    private FrozenRecordDao frozenRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param frozenRecord
     */
    @Override
    public void add(final FrozenRecord frozenRecord) {
        this.frozenRecordDao.insert(frozenRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param frozenRecord
     * @return
     */
    @Override
    public int update(final FrozenRecord frozenRecord) {
        return this.frozenRecordDao.update(frozenRecord);
    }
    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<FrozenRecord> getById(final long id) {
        return Optional.fromNullable(this.frozenRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<FrozenRecord> getByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.frozenRecordDao.selectByIds(ids);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @return
     */
    @Override
    public List<FrozenRecord> getByAccountId(final long accountId) {
        return this.frozenRecordDao.selectByAccountId(accountId);
    }

    /**
     * {@inheritDoc}
     *
     * @param businessNo
     * @return
     */
    @Override
    public Optional<FrozenRecord> getByBusinessNo(String businessNo) {
        return Optional.fromNullable(this.frozenRecordDao.selectByBusinessNo(businessNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param businessNo
     */
    @Override
    public void updateStatusByBusinessNo(String businessNo, EnumFrozenStatus status) {
        this.frozenRecordDao.updateStatusByBusinessNo(businessNo, status.getId());
    }
}
