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
    List<ShallProfitDetail> selectDeatailByProfitDate(String profitDate);

    /**
     * 查询商户id
     * @param profitDate
     * @return
     */
    List<Long> selectMerchantIdByProfitDate(String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondCollectMoneyByMerchantIdAndProfitDate(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstCollectMoneyByMerchantIdAndProfitDate(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstWithdrawMoneyByMerchantIdAndProfitDate(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondWithdrawMoneyByMerchantIdAndProfitDate(@Param("merchantId") Long merchantId, @Param("profitDate") String profitDate);

    /**
     *
     * @param profitDate
     * @return
     */
    List<Long> selectDealerIdByProfitDate(String profitDate);

    /**
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstCollectMoneyByDealerIdAndProfitDate(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstWithdrawMoneyByDealerIdAndProfitDate(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondYesterdayProfitMoney(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondYesterdayDealMoney(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @param
     * @return
     */
    BigDecimal selectSecondHistoryProfitMoney(@Param("dealerId") long dealerId);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstSecondYesterdayProfitMoney(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @return
     */
    BigDecimal selectFirstSecondHistoryProfitMoney(@Param("dealerId") long dealerId);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstMerchantYesterdayProfitMoney(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param dealerId
     * @return
     */
    BigDecimal selectFirstMerchantHistoryProfitMoney(@Param("dealerId") long dealerId);

    /**
     * 查询
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstYesterdayDealMoney(@Param("dealerId") long dealerId, @Param("profitDate") String profitDate);

    /**
     * 查询
     * @param orderId
     * @return
     */
    ShallProfitDetail selectByOrderId(@Param("orderId") String orderId);
}
