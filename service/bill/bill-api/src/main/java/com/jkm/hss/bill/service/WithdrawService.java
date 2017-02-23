package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
public interface WithdrawService {


    /**
     * 代理商按结算单提现
     *
     * @param merchantId
     * @param settlementRecordId 结算单id
     * @param payOrderSn 支付中心支付流水号
     * @param payChannelSign 结算周期
     * @return
     */
    Pair<Integer, String> merchantWithdrawBySettlementRecord(long merchantId, long settlementRecordId, String payOrderSn, int payChannelSign);

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
