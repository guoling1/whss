package com.jkm.hss.bill.service;

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
}
