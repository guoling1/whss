package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;


/**
 * Created by yuxiang on 2017-03-08.
 */
public interface MerchantWithdrawService {

    /**
     * 提现
     *
     * @param accountId
     * @param totalAmount
     * @param channel
     * @return
     */

    Pair<Integer, String> withdraw(long accountId, String totalAmount, int channel, String appId, BigDecimal withDrawFee);

    /**
     * 提现回调
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    void handleDealerWithdrawCallbackMsg(PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse);
}
