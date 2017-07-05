package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.JkmAccountDao;
import com.jkm.hss.merchant.service.JkmAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-30 14:14
 */
@Slf4j
@Service
public class JkmAccountServiceImpl implements JkmAccountService {
    @Autowired
    private JkmAccountDao jkmAccountDao;
    @Override
    public int income(BigDecimal money, BigDecimal totalMoney, long id) {
        return jkmAccountDao.income(money,totalMoney,id);
    }

    @Override
    public int outMoney(BigDecimal money, long id) {
        return jkmAccountDao.outMoney(money,id);
    }
}
