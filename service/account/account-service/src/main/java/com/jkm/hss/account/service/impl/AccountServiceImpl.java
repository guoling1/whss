package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.AccountDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * {@inheritDoc}
     *
     * @param account
     */
    @Override
    public void add(final Account account) {
        this.accountDao.insert(account);
    }

    /**
     * {@inheritDoc}
     *
     * @param userName
     * @return
     */
    @Override
    public long initAccount(final String userName) {
        final Account account = new Account();
        account.setUserName(userName);
        this.add(account);
        return account.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param account
     * @return
     */
    @Override
    public int update(final Account account) {
        return this.accountDao.update(account);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Account> getById(final long id) {
        return Optional.fromNullable(this.accountDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Account> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.accountDao.selectByIdWithLock(id));
    }
}


