package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PayCallbackResponse;
import com.jkm.hss.merchant.entity.MerchantInfo;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
public interface PayService {

    /**
     * 动态码收款
     *
     * @param totalAmount
     * @param channel  通道
     * @param merchantId
     * @return
     */
    Pair<Integer, String> codeReceipt(String totalAmount, int channel, long merchantId);

    /**
     * 代理商提现
     *
     * @param merchantId
     * @param tradePeried 结算周期
     * @return
     */
    Pair<Integer, String> merchantWithdraw(long merchantId, String tradePeried);

    /**
     * 处理支付中心， 支付结果回调
     *
     * @param payCallbackResponse
     */
    void handlePayCallbackMsg(PayCallbackResponse payCallbackResponse);

    /**
     * 将交易订单标记为 支付成功
     *
     * @param payCallbackResponse
     * @param order
     */
    void markPaySuccess(PayCallbackResponse payCallbackResponse, Order order);


    /**
     * 将交易订单标记为 支付失败
     *
     * @param payCallbackResponse
     * @param order
     */
    void markPayFail(PayCallbackResponse payCallbackResponse, Order order);

    /**
     * 商户入账
     *  1.入账到商户的账户待结算余额
     *  2.入账到手续费账户待结算余额
     *
     * @param orderId
     * @param merchant
     */
    void merchantRecorded(long orderId, MerchantInfo merchant);

    /**
     * 商户结算进入到可用余额
     *
     * @param orderId
     * @param merchantId
     */
    void merchantSettle(long orderId, long merchantId);

    /**
     * 手续费结算 到 代理商待结算账户
     *
     * @param orderId
     * @param merchantId
     */
    void poundageSettle(long orderId, long merchantId);
}
