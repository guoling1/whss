package com.jkm.hss.dealer.service.impl;

import com.jkm.hss.dealer.dao.CompanyProfitDetailDao;
import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import com.jkm.hss.dealer.service.CompanyProfitDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-12-05.
 */
@Service
public class CompanyProfitDetailServiceImpl implements CompanyProfitDetailService {

    @Autowired
    private CompanyProfitDetailDao companyProfitDetailDao;

    /**
     * {@inheritDoc}
     * @param companyProfitDetail
     */
    @Override
    public void add(CompanyProfitDetail companyProfitDetail) {
        this.companyProfitDetailDao.add(companyProfitDetail);
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectCollectProfitByProfitDateToHss(String profitDate) {
        return this.companyProfitDetailDao.selectCollectProfitByProfitDateToHss(profitDate);
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectWithdrawProfitByProfitDate(String profitDate) {
        return this.companyProfitDetailDao.selectWithdrawProfitByProfitDateToHss(profitDate);
    }


    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public List<CompanyProfitDetail> selectByProfitDateToHss(String profitDate) {
        return this.companyProfitDetailDao.selectByProfitDateToHss(profitDate);
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public List<Long> getMerchantIdByProfitDateToHss(String profitDate) {
        return this.companyProfitDetailDao.getMerchantIdByProfitDateToHss(profitDate);
    }
}
