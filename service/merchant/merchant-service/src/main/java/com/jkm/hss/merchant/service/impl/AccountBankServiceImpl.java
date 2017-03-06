package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.AccountBankDao;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.service.AccountBankService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/3/6.
 */
@Slf4j
@Service
public class AccountBankServiceImpl implements AccountBankService{
    @Autowired
    private AccountBankDao accountBankDao;
    /**
     * 新增
     *
     * @param accountBank
     * @return
     */
    @Override
    public int insert(AccountBank accountBank) {
        return accountBankDao.insert(accountBank);
    }

    /**
     * 修改
     *
     * @param accountBank
     * @return
     */
    @Override
    public int update(AccountBank accountBank) {
        return accountBankDao.update(accountBank);
    }

    /**
     * 设置为默认银行卡
     *
     * @param id
     * @return
     */
    @Override
    public int setDefault(long id) {
        return accountBankDao.setDefault(id);
    }

    /**
     * 全部设置为不是默认银行卡
     *
     * @param accountId
     * @return
     */
    @Override
    public int reset(long accountId) {
        return accountBankDao.reset(accountId);
    }

    /**
     * 获取默认银行卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountBank getDefault(long accountId) {
        return accountBankDao.getDefault(accountId);
    }

    /**
     * 获取信用卡信息
     *
     * @param accountId
     * @return
     */
    @Override
    public AccountBank getCreditCard(long accountId) {
        return accountBankDao.getCreditCard(accountId);
    }

    /**
     * 是否有信用卡
     *
     * @param accountId
     * @return
     */
    @Override
    public int isHasCreditCard(long accountId) {
        return accountBankDao.isHasCreditCard(accountId);
    }
}
