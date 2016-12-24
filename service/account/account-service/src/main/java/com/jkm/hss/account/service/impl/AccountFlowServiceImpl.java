package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.AccountFlowDao;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.sevice.AccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class AccountFlowServiceImpl implements AccountFlowService {

    @Autowired
    private AccountFlowDao accountFlowDao;

    /**
     * {@inheritDoc}
     *
     * @param accountFlow
     */
    @Override
    public void add(final AccountFlow accountFlow) {
        this.accountFlowDao.insert(accountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AccountFlow> getById(final long id) {
        return Optional.fromNullable(this.accountFlowDao.selectById(id));
    }
}
