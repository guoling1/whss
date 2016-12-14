package com.jkm.hss.dealer.service.impl;

import com.jkm.hss.dealer.dao.CompanyProfitDetailDao;
import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import com.jkm.hss.dealer.service.CompanyProfitDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
