package com.jkm.hss.bill.service;

import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
public interface CalculateService {

    /**
     * 查询商户支付手续费率
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    BigDecimal getMerchantPayPoundageRate(long merchantId, int channelSign);

    /**
     * 查询商户提现手续
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    BigDecimal getMerchantWithdrawPoundage(long merchantId, int channelSign);

    /**
     * 计算商户的支付手续费
     *
     * @param traderAmount  交易金额
     * @param merchantPayPoundageRate  商户支付手续费率
     * @return
     */
    BigDecimal getMerchantPayPoundage(BigDecimal traderAmount, BigDecimal merchantPayPoundageRate);

    /**
     * 获取当前商户升级，所有分给（代理商，推荐人）的钱--记录为手续费
     *
     * @param merchantId
     * @return
     */
    BigDecimal getMerchantUpgradePoundage(long merchantId , String orderNo ,BigDecimal tradeAmount, String businessNo);
}
