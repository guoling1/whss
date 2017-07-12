package com.jkm.hss.bill.service;


import com.jkm.hss.product.enums.EnumProductType;

import java.math.BigDecimal;

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
    BigDecimal getMerchantPayPoundageRate(EnumProductType type, long merchantId, int channelSign);

    /**
     * 查询商户提现手续
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    BigDecimal getMerchantWithdrawPoundage(EnumProductType type,long merchantId, int channelSign);

    /**
     * 计算商户的支付手续费
     *
     * @param traderAmount  交易金额
     * @param merchantPayPoundageRate  商户支付手续费率
     * @return
     */
    BigDecimal getMerchantPayPoundage(BigDecimal traderAmount, BigDecimal merchantPayPoundageRate, int channelSign);

    /**
     * 获取当前商户升级，所有分给（代理商，推荐人）的钱--记录为手续费
     *
     * @param merchantId
     * @return
     */
    BigDecimal getMerchantUpgradePoundage(long merchantId , String orderNo ,BigDecimal tradeAmount, String businessNo);
}
