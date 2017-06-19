package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.MemberAccountDao;
import com.jkm.hss.account.entity.MemberAccount;
import com.jkm.hss.account.sevice.MemberAccountService;
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
public class MemberAccountServiceImpl implements MemberAccountService {

    @Autowired
    private MemberAccountDao memberAccountDao;
    /**
     * {@inheritDoc}
     *
     * @param memberAccount
     */
    @Override
    @Transactional
    public void add(final MemberAccount memberAccount) {
        this.memberAccountDao.insert(memberAccount);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    @Transactional
    public long init() {
        final MemberAccount memberAccount = new MemberAccount();
        memberAccount.setAvailable(new BigDecimal("0.00"));
        memberAccount.setConsumeTotalAmount(new BigDecimal("0.00"));
        memberAccount.setRechargeTotalAmount(new BigDecimal("0.00"));
        memberAccount.setStatus(0);
        this.add(memberAccount);
        return memberAccount.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param memberAccount
     * @return
     */
    @Override
    @Transactional
    public int update(final MemberAccount memberAccount) {
        return this.memberAccountDao.update(memberAccount);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberAccount> getById(final long id) {
        return Optional.fromNullable(this.memberAccountDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberAccount> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.memberAccountDao.selectByIdWithLock(id));
    }
}
