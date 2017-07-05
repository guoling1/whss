package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

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

    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectCollectProfitByProfitDateToHss(String profitDate);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectWithdrawProfitByProfitDateToHss(String profitDate);


    /**
     * 查询
     * @param profitDate
     * @return
     */
    List<CompanyProfitDetail> selectByProfitDateToHss(String profitDate);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    List<Long> getMerchantIdByProfitDateToHss(String profitDate);
}
