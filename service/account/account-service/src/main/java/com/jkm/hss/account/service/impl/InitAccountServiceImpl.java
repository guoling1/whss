package com.jkm.hss.account.service.impl;

import com.jkm.hss.account.dao.AccountDao;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.helper.AccountConstants;
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
        final Account poundageAccount1 = this.accountDao.selectById(AccountConstants.POUNDAGE_ACCOUNT_ID);
        if (null == poundageAccount1) {
            final Account poundageAccount = new Account();
            poundageAccount.setId(AccountConstants.POUNDAGE_ACCOUNT_ID);
            poundageAccount.setUserName("好收收手续费账户");
            poundageAccount.setTotalAmount(new BigDecimal("0.00"));
            poundageAccount.setAvailable(new BigDecimal("0.00"));
            poundageAccount.setFrozenAmount(new BigDecimal("0.00"));
            poundageAccount.setDueSettleAmount(new BigDecimal("0.00"));
            this.accountDao.initPoundageAccount(poundageAccount);
        }
        final Account jkmAccount1 = this.accountDao.selectById(AccountConstants.JKM_ACCOUNT_ID);
        if (null == jkmAccount1) {
            final Account jkmAccount = new Account();
            jkmAccount.setId(AccountConstants.JKM_ACCOUNT_ID);
            jkmAccount.setUserName("金开门利润账户");
            jkmAccount.setTotalAmount(new BigDecimal("0.00"));
            jkmAccount.setAvailable(new BigDecimal("0.00"));
            jkmAccount.setFrozenAmount(new BigDecimal("0.00"));
            jkmAccount.setDueSettleAmount(new BigDecimal("0.00"));
            this.accountDao.initPoundageAccount(jkmAccount);
        }

        final Account dealerPoundageAccount1 = this.accountDao.selectById(AccountConstants.DEALER_POUNDAGE_ACCOUNT_ID);
        if (null == dealerPoundageAccount1) {
            final Account jkmAccount = new Account();
            jkmAccount.setId(AccountConstants.DEALER_POUNDAGE_ACCOUNT_ID);
            jkmAccount.setUserName("代理商提现手续费账户");
            jkmAccount.setTotalAmount(new BigDecimal("0.00"));
            jkmAccount.setAvailable(new BigDecimal("0.00"));
            jkmAccount.setFrozenAmount(new BigDecimal("0.00"));
            jkmAccount.setDueSettleAmount(new BigDecimal("0.00"));
            this.accountDao.initPoundageAccount(jkmAccount);
        }
    }
}
