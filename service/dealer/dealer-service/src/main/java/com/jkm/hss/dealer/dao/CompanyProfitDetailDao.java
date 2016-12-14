package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import org.springframework.stereotype.Repository;

/**
 * Created by yuxiang on 2016-12-05.
 */
@Repository
public interface CompanyProfitDetailDao {

    /**
     * 新增
     * @param companyProfitDetail
     */
    void add(CompanyProfitDetail companyProfitDetail);
}
