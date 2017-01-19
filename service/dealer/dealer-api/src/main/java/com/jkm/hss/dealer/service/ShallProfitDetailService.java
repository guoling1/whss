package com.jkm.hss.dealer.service;

import com.jkm.hss.dealer.entity.ShallProfitDetail;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.product.enums.EnumProductType;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2016-11-24.
 */
public interface ShallProfitDetailService {

    /**
     * 初始化
     * @param shallProfitDetail
     */
    void init(ShallProfitDetail shallProfitDetail);

    /**
     * 提现分润
     *
     * @param orderNo
     * @param tradeAmount
     * @param channelSign
     * @param merchantId
     * @return
     */
    Map<String, Triple<Long, BigDecimal, String>> withdrawProfitCount(EnumProductType type, String orderNo, BigDecimal tradeAmount,
                                                                      int channelSign, long merchantId);

    /**
     * 提现需要的参数 (手续费,通道成本)
     * @param merchantId
     * @return
     */
    Pair<BigDecimal, BigDecimal> withdrawParams(long merchantId, int payChannel);

    /**
     * 查询昨日有分润记录的商户
     * @param profitDate
     * @return
     */
    List<ShallProfitDetail> selectDeatailByProfitDateToHss(String profitDate);

    /**
     * 查询昨日有分润记录的商户id
     * @param profitDate
     * @return
     */
    List<Long> selectMerchantIdByProfitDateToHss(String profitDate);

    /**
     * 查询某个商户某天的收单分润
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondCollectMoneyByMerchantIdAndProfitDateToHss(Long merchantId, String profitDate);

    /**
     * 查询某个商户某天的收单分润
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstCollectMoneyByMerchantIdAndProfitDateToHss(Long merchantId, String profitDate);

    /**
     * 查询某个商户一级提现分润
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstWithdrawMoneyByMerchantIdAndProfitDateToHss(Long merchantId, String profitDate);

    /**
     * 查询某个商户二级提现分润
     * @param merchantId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondWithdrawMoneyByMerchantIdAndProfitDateToHss(Long merchantId, String profitDate);

    /**
     *查询二级代理商id
     * @param profitDate
     * @return
     */
    List<Long> selectDealerIdByProfitDateToHss(String profitDate);

    /**
     *
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstCollectMoneyByDealerIdAndProfitDateToHss(long dealerId, String profitDate);

    /**
     *
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstWithdrawMoneyByDealerIdAndProfitDateToHss(long dealerId, String profitDate);

    /**
     * 查询二级昨日分润总额
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondYesterdayProfitMoneyToHss(long dealerId, String profitDate);

    /**
     * 查询二级昨日店铺交易总额
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectSecondYesterdayDealMoneyToHss(long dealerId, String profitDate);

    /**
     * 查询二级历史分润总额
     * @param
     * @param dealerId
     * @return
     */
    BigDecimal selectSecondHistoryProfitMoneyToHss(long dealerId);

    /**
     * 我的分润-二级代理 昨日分润金额
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstSecondYesterdayProfitMoneyToHss(long dealerId, String profitDate);

    /**
     * 我的分润-二级代理 历史分润总额
     *
     * @param dealerId
     * @return
     */
    BigDecimal selectFirstSecondHistoryProfitMoneyToHss(long dealerId);

    /**
     * 我的分润-直管店铺 昨日分润金额
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstMerchantYesterdayProfitMoneyToHss(long dealerId, String profitDate);

    /**
     * 我的分润-直管店铺 历史分润总额
     *
     * @param dealerId
     * @return
     */
    BigDecimal selectFirstMerchantHistoryProfitMoneyToHss(long dealerId);

    /**
     * 昨日店铺交易总额(一级)
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    BigDecimal selectFirstYesterdayDealMoneyToHss(long dealerId, String profitDate);

    /**
     * 查询
     * @param orderId
     * @return
     */
    ShallProfitDetail selectByOrderIdToHss(String orderId);

    /**
     * 查询公司收单分润
     * @param profitDate
     * @return
     */
    BigDecimal selectCompanyCollectProfitByProfitDateToHss(String profitDate);

    /**
     * 查询公司体现分润
     * @param profitDate
     * @return
     */
    BigDecimal selectCompanyWithdrawProfitByProfitDateToHss(String profitDate);

    /**
     * 查询二级代理每日分润明细
     * @param dailyProfitId
     * @return
     */
    List<ShallProfitDetail> getSecondDealerDetailToHss(long dailyProfitId);

    /**
     * 查询一级代理每日分润明细
     * @param dailyProfitId
     * @return
     */
    List<ShallProfitDetail> getFirstDealerDetailToHss(long dailyProfitId);

    /**
     * 查询
     * @param profitDate
     * @return
     */
    List<ShallProfitDetail> selectCompanyByProfitDateToHss(String profitDate);

    /**
     * 查询
     * @param statisticsDate
     * @return
     */
    List<Long> getMerchantIdByProfitDateToHss(String statisticsDate);
}
