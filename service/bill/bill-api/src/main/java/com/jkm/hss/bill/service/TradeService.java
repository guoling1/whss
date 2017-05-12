package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.helper.PayParams;
import com.jkm.hss.bill.helper.RechargeParams;
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
    Pair<Integer, String> recharge(RechargeParams rechargeParams);

    /**
     * 支付
     *
     * @param payParams
     * @return
     */
    Pair<Integer, String> pay(PayParams payParams);

    /**
     * 处理支付中心，支付/充值回调
     *
     * @param paymentSdkPayCallbackResponse
     */
    void handlePayOrRechargeCallbackMsg(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse);


    Pair<Integer, String> withdraw();
}
