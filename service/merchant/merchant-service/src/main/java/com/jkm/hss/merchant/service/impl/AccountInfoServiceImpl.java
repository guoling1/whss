package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.AccountInfoDao;
import com.jkm.hss.merchant.entity.AccountInfo;
import com.jkm.hss.merchant.service.AccountInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 14:08
 */
@Slf4j
@Service
public class AccountInfoServiceImpl implements AccountInfoService{
    @Autowired
    private AccountInfoDao accountInfoDao;
    @Override
    public AccountInfo selectByPrimaryKey(Long id) {
        return accountInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(AccountInfo record) {
        return accountInfoDao.insertSelective(record);
    }

    @Override
    @Transactional
    public long addNewAccount() {
        AccountInfo accountInfo = new AccountInfo();
        accountInfo.setStatus(0);
        accountInfo.setAmount(new BigDecimal(0));
        accountInfo.setAvailable(new BigDecimal(0));
        accountInfo.setFrozenAmount(new BigDecimal(0));
        accountInfo.setUnsettled(new BigDecimal(0));
        accountInfoDao.insertSelective(accountInfo);
        return accountInfo.getId();
    }


    @Override
    public int updateByPrimaryKeySelective(AccountInfo record) {
        return accountInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int addAvailableMoney(long id, BigDecimal available) {
        return accountInfoDao.addAvailableMoney(id,available);
    }

    @Override
    public int addUnsettledMoney(long id, BigDecimal unsettled) {
        return accountInfoDao.addUnsettledMoney(id,unsettled);
    }

    @Override
    public int frozenMoney(BigDecimal frozenAmount, Long id) {
        return accountInfoDao.frozenMoney(frozenAmount,id);
    }

    @Override
    public int inMoney(BigDecimal frozenAmount, Long id) {
        return accountInfoDao.inCome(frozenAmount,id);
    }

    @Override
    public int backMoney(BigDecimal frozenAmount, Long id) {
        return accountInfoDao.backMoney(frozenAmount,id);
    }
}
