package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
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
     * 处理支付中心， 支付结果回调
     *
     * @param paymentSdkPayCallbackResponse
     */
    void handlePayCallbackMsg(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse);

    /**
     * 将交易订单标记为 支付成功
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markPaySuccess(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);


    /**
     * 将交易订单标记为 支付失败
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markPayFail(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

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
     * @param order
     * @param merchantId
     */
    void merchantSettle(Order order, long merchantId);

    /**
     * 手续费结算 到 代理商待结算账户
     *
     * @param order
     * @param merchantId
     */
    void poundageSettle(Order order, long merchantId);
}
