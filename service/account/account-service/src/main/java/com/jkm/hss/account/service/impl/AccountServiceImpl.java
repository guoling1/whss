package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.AccountDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
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
        account.setDueSettleAmount(new BigDecimal("0.00"));
        account.setFrozenAmount(new BigDecimal("0.00"));
        account.setAvailable(new BigDecimal("0.00"));
        account.setTotalAmount(new BigDecimal("0.00"));
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
     * @param amount
     * @return
     */
    @Override
    public int increaseTotalAmount(final long id, final BigDecimal amount) {
        return this.accountDao.increaseTotalAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int decreaseTotalAmount(final long id, final BigDecimal amount) {
        return this.accountDao.decreaseTotalAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int increaseAvailableAmount(final long id, final BigDecimal amount) {
        return this.accountDao.increaseAvailableAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int decreaseAvailableAmount(final long id, final BigDecimal amount) {
        return this.accountDao.decreaseAvailableAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int increaseFrozenAmount(final long id, final BigDecimal amount) {
        return this.accountDao.increaseFrozenAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int decreaseFrozenAmount(final long id, final BigDecimal amount) {
        return this.accountDao.decreaseFrozenAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int increaseSettleAmount(final long id, final BigDecimal amount) {
        return this.accountDao.increaseSettleAmount(id, amount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @return
     */
    @Override
    public int decreaseSettleAmount(final long id, final BigDecimal amount) {
        return this.accountDao.decreaseSettleAmount(id, amount);
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


