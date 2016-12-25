package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.InitAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
@Slf4j
@Service
public class InitAccountServiceImpl implements InitAccountService {

    @Autowired
    private AccountService accountService;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        final Optional<Account> accountOptional = this.accountService.getById(1);
        if (!accountOptional.isPresent()) {
            final Account account = new Account();
            account.setId(1);
            account.setUserName("好收收手续费账户");
            this.accountService.add(account);
        }
    }
}
