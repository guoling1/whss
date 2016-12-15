package com.jkm.hss.dealer.service;

import com.jkm.hss.dealer.entity.CompanyProfitDetail;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-12-05.
 */
public interface CompanyProfitDetailService {

    void add(final CompanyProfitDetail companyProfitDetail);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectCollectProfitByProfitDate(String profitDate);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectWithdrawProfitByProfitDate(String profitDate);
}
