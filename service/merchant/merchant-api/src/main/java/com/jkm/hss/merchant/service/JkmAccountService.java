package com.jkm.hss.merchant.service;

import java.math.BigDecimal;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-30 14:13
 */
public interface JkmAccountService {
    int  income(BigDecimal money, BigDecimal totalMoney,long id);
    int  outMoney(BigDecimal money,long id);
}
