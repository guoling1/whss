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
    BigDecimal selectCollectProfitByProfitDate(@Param("profitDate") String profitDate);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectWithdrawProfitByProfitDate(@Param("profitDate") String profitDate);


    /**
     * 查询
     * @param profitDate
     * @return
     */
    List<CompanyProfitDetail> selectByProfitDate(@Param("profitDate") String profitDate);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    List<Long> getMerchantIdByProfitDate(@Param("profitDate") String profitDate);
}
