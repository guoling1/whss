package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.helper.*;
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
    Pair<Integer, String> refundProfitImpl(RefundProfitParams refundProfitParams);

    Pair<Integer, String> withdraw();
}
