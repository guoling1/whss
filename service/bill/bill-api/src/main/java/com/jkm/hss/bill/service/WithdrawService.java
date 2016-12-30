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
     * @param payOrderId 交易单（支付单）id
     * @param payOrderSn 支付中心支付流水号
     * @param tradePeriod 结算周期
     * @return
     */
    Pair<Integer, String> merchantWithdrawByOrder(long merchantId, long payOrderId, String payOrderSn, String tradePeriod);

    /**
     * 按账单提现
     *
     * @param merchantId
     * @param playMoneyOrderId
     * @param payOrderSn 支付中心支付流水号
     * @param tradePeriod
     * @return
     */
    Pair<Integer, String> withdrawByOrder(long merchantId, long playMoneyOrderId, String payOrderSn, String tradePeriod);


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
     * @param accountId
     */
    void merchantPoundageSettle(Order order, long accountId);
}
