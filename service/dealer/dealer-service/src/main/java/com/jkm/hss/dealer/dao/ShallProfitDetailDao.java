package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.ShallProfitDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-24.
 */
@Repository
public interface ShallProfitDetailDao {

    /**
     * 初始化
     * @param shallProfitDetail
     */
    void init(ShallProfitDetail shallProfitDetail);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    List<ShallProfitDetail> selectDetailByProfitDateToHss(String profitDate);

    /**
     * 查询商户id
     * @param profitDate
     * @return
     */
    List<Long> selectMerchantIdByProfitDateToHss(String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondCollectMoneyByMerchantIdAndProfitDateToHss(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstCollectMoneyByMerchantIdAndProfitDateToHss(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstWithdrawMoneyByMerchantIdAndProfitDateToHss(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondWithdrawMoneyByMerchantIdAndProfitDateToHss(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param profitDate
     * @return
     */
    List<Long> selectDealerIdByProfitDateToHss(String profitDate);

    /**
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstCollectMoneyByDealerIdAndProfitDateToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstWithdrawMoneyByDealerIdAndProfitDateToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondYesterdayProfitMoneyToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondYesterdayDealMoneyToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param
     * @param dealerId
     * @return
     */
    BigDecimal selectSecondHistoryProfitMoneyToHss(@Param("dealerId") long dealerId);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstSecondYesterdayProfitMoneyToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @return
     */
    BigDecimal selectFirstSecondHistoryProfitMoneyToHss(@Param("dealerId") long dealerId);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstMerchantYesterdayProfitMoneyToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @return
     */
    BigDecimal selectFirstMerchantHistoryProfitMoneyToHss(@Param("dealerId") long dealerId);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstYesterdayDealMoneyToHss(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param orderId
     * @return
     */
    ShallProfitDetail selectByOrderIdToHss(@Param("orderId") String orderId);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectCompanyCollectProfitByProfitDateToHss(@Param("profitDate") String profitDate);


    /**
     * 查询
     * @param profitDate
     * @return
     */
    BigDecimal selectCompanyWithdrawProfitByProfitDateToHss(@Param("profitDate") String profitDate);

    /**
     * 查询二代每日分润详情
     * @param secondDealerId
     * @param statisticsDate
     * @return
     */
    List<ShallProfitDetail> selectByProfitDateAndSecondDealerIdToHss(@Param("secondDealerId") long secondDealerId, @Param("statisticsDate") String statisticsDate);

    /**
     * 查询一代每日分润详情
     * @param firstDealerId
     * @param statisticsDate
     * @return
     */
    List<ShallProfitDetail> selectByProfitDateAndFirstDealerIdToHss(@Param("firstDealerId") long firstDealerId, @Param("statisticsDate") String statisticsDate);


    /**
     * 查询公司
     * @param profitDate
     * @return
     */
    List<ShallProfitDetail> selectCompanyByProfitDateToHss(String profitDate);

    /**
     *
     * @param statisticsDate
     * @return
     */
    List<Long> getMerchantIdByProfitDateToHss(String statisticsDate);
}
