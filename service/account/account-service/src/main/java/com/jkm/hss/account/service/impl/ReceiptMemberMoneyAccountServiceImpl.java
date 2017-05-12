package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.ReceiptMemberMoneyAccountDao;
import com.jkm.hss.account.entity.ReceiptMemberMoneyAccount;
import com.jkm.hss.account.sevice.ReceiptMemberMoneyAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Slf4j
@Service
public class ReceiptMemberMoneyAccountServiceImpl implements ReceiptMemberMoneyAccountService {

    @Autowired
    private ReceiptMemberMoneyAccountDao receiptMemberMoneyAccountDao;

    /**
     * {@inheritDoc}
     *
     * @param receiptMemberMoneyAccount
     */
    @Override
    @Transactional
    public void add(final ReceiptMemberMoneyAccount receiptMemberMoneyAccount) {
        this.receiptMemberMoneyAccountDao.insert(receiptMemberMoneyAccount);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public long init() {
        final ReceiptMemberMoneyAccount receiptMemberMoneyAccount = new ReceiptMemberMoneyAccount();
        receiptMemberMoneyAccount.setRechargeTotalAmount(new BigDecimal("0.00"));
        receiptMemberMoneyAccount.setIncomeToTalAmount(new BigDecimal("0.00"));
        receiptMemberMoneyAccount.setStatus(0);
        this.add(receiptMemberMoneyAccount);
        return receiptMemberMoneyAccount.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param receiptMemberMoneyAccount
     * @return
     */
    @Override
    @Transactional
    public int update(final ReceiptMemberMoneyAccount receiptMemberMoneyAccount) {
        return this.receiptMemberMoneyAccountDao.update(receiptMemberMoneyAccount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param income
     * @return
     */
    @Override
    @Transactional
    public int increaseIncomeAmount(final long id, final BigDecimal income) {
        return this.receiptMemberMoneyAccountDao.increaseIncomeAmount(id, income);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ReceiptMemberMoneyAccount> getById(final long id) {
        return Optional.fromNullable(this.receiptMemberMoneyAccountDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<ReceiptMemberMoneyAccount> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.receiptMemberMoneyAccountDao.selectByIdWithLock(id));
    }
}
