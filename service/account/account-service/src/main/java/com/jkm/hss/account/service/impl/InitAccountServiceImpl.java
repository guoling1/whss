package com.jkm.hss.account.service.impl;

import com.jkm.hss.account.dao.AccountDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.InitAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
@Slf4j
@Service
public class InitAccountServiceImpl implements InitAccountService {

    @Autowired
    private AccountDao accountDao;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void init() {
        final Account account1 = this.accountDao.selectById(1);
        if (null == account1) {
            final Account account = new Account();
            account.setId(1);
            account.setUserName("好收收手续费账户");
            account.setTotalAmount(new BigDecimal("0.00"));
            account.setAvailable(new BigDecimal("0.00"));
            account.setFrozenAmount(new BigDecimal("0.00"));
            account.setDueSettleAmount(new BigDecimal("0.00"));
            this.accountDao.initPoundageAccount(account);
        }
    }
}
