package com.jkm.hss.bill.service;

import com.jkm.hss.bill.helper.CallbackResponse;
import com.jkm.hsy.user.entity.AppParam;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/6/12.
 */
public interface HSYTransactionService {

    /**
     * 创建订单
     *
     * @param channel
     * @param shopId
     * @param memberId
     * @param code
     * @return
     */
    long createOrder(int channel, long shopId, String memberId, String code);

    /**
     * 创建订单
     *
     * @param shopId
     * @param amount
     * @return
     */
    long createOrder2(long shopId, long currentUid, BigDecimal amount);

    /**
     * 好收银扫码下单
     *
     * @param totalAmount 订单金额
     * @param hsyOrderId 订单id
     * @return
     */
    Triple<Integer, String, String> placeOrder(String totalAmount, long hsyOrderId);

    /**
     * 支付回调
     *
     * @param callbackResponse
     */
    void handlePayCallbackMsg(CallbackResponse callbackResponse);

    /**
     * 支付消息分润
     *
     * @param consumeMsgSplitProfitRecordId
     */
    void paySplitProfit(long consumeMsgSplitProfitRecordId);

    /**
     * 对于
     */
    void handleRetrySplitProfitTask();

    /**
     * 退款
     *
     * @param paramData
     * @param appParam
     * @return
     */
    String refund(String paramData, AppParam appParam);

    /**
     * 授权码支付
     *
     * @param orderId
     * @param authCode
     * @return
     */
    Pair<Integer,String> authCodePay(long orderId, String authCode);
}
