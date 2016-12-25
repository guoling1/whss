package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
public interface WithdrawService {


    /**
     * 代理商提现
     *
     * @param merchantId
     * @param tradePeriod 结算周期
     * @return
     */
    Pair<Integer, String> merchantWithdraw(long merchantId, String tradePeriod);

    /**
     * 提现回调
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    void handleWithdrawCallbackMsg(PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse);

    /**
     * 商户提现结算
     *
     * @param order
     * @param merchantId
     */
    void merchantPoundageSettle(Order order, long merchantId);
}
