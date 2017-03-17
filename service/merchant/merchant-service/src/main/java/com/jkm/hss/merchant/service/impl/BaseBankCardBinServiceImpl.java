package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.BankCardBinDao;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.service.BaseBankCardBinService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangbin on 2016/11/29.
 */
@Slf4j
@Service
public class BaseBankCardBinServiceImpl implements BaseBankCardBinService {

    @Autowired
    private BankCardBinDao bankCardBinDao;

    @Override
    public Optional<BankCardBin> loadByBinNo(final String bankNo) {
        return Optional.fromNullable(bankCardBinDao.loadByBinNo(bankNo));
    }
    /**
     * 查询信用卡
     * @param bankNo
     * @return
     */
    @Override
    public Optional<BankCardBin> loadByBinNoAndType(String bankNo) {
        return Optional.fromNullable(bankCardBinDao.loadByBinNoAndType(bankNo));
    }
}
