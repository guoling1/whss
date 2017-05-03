package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderResponse;
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
     * @param receiptMemberMoneyAccountId
     * @param order
     */
    void memberPayImpl(long receiptMemberMoneyAccountId, Order order);
}
