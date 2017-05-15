package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.helper.PlaceOrderParams;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/5/3.
 */
public interface BaseTradeService {

    /**
     * 请求下单
     *
     * @param placeOrderParams
     * @param order 交易单
     * @return
     */
    PaymentSdkPlaceOrderResponse requestPlaceOrder(PlaceOrderParams placeOrderParams, Order order);

    /**
     * 处理下单结果
     *
     * @param placeOrderResponse
     * @param merchantPayType
     * @param order
     * @return
     */
    Pair<Integer, String> handlePlaceOrderResult(PaymentSdkPlaceOrderResponse placeOrderResponse, EnumMerchantPayType merchantPayType, Order order);

    /**
     * 会员付款实现
     *
     * @param orderId
     */
    Pair<Integer, String> memberPayImpl(long orderId);

    /**
     * 支付/充值-支付中心回调实现
     *
     * @param paymentSdkPayCallbackResponse
     * @param orderId
     */
    void handlePayOrRechargeCallbackMsgImpl(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, long orderId);

    /**
     * 支付-支付中心回调实现
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void handlePayCallbackMsgImpl(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 充值-支付中心回调实现
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void handleRechargeCallbackMsgImpl(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 标记为支付成功
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markPaySuccess(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 标记为充值成功
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markRechargeSuccess(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 标记为支付失败
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markPayFail(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 标记为充值失败
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markRechargeFail(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 标记为支付处理中
     * 
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markPayHandling(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 标记为充值处理中
     * 
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markRechargeHandling(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);
    
    /**
     * 支付入账
     *
     * @param orderId
     */
    long record(long orderId);

    /**
     * 充值成功，会员账户额度增加
     *
     * @param order
     */
    void memberAccountIncrease(Order order);
}
