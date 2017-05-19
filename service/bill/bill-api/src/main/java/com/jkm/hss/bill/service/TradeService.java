package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.helper.*;
import com.jkm.hss.bill.helper.responseparam.RefundProfitResponse;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/5/2.
 *
 * 交易
 */
public interface TradeService {
    /**
     * 充值
     *
     * @param rechargeParams
     * @return
     */
    PayResponse recharge(RechargeParams rechargeParams);

    /**
     * 支付
     *
     * @param payParams
     * @return
     */
    PayResponse pay(PayParams payParams);

    /**
     * 处理支付中心，支付/充值回调
     *
     * @param paymentSdkPayCallbackResponse
     */
    void handlePayOrRechargeCallbackMsg(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse);

    /**
     * 分润实现
     *
     * @param splitProfitParams
     * @return
     */
    Pair<Integer, String> splitProfitImpl(SplitProfitParams splitProfitParams);

    /**
     * 退分润实现
     *
     * @param refundProfitParams
     * @return
     */
    RefundProfitResponse refundProfitImpl(RefundProfitParams refundProfitParams);

    /**
     * 退款
     *
     * @param refundOrderNo
     * @return
     */
    Pair<Integer, String> refund(String refundOrderNo);

    /**
     *
     *
     * @param withdrawParams
     * @return
     */
    Pair<Integer, String> withdraw(WithdrawParams withdrawParams);

    /**
     * 处理提现回调
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    void  handleWithdrawCallbackMsg(PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse);
}
