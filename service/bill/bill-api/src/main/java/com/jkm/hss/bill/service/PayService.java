package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.merchant.entity.MerchantInfo;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
public interface PayService {

    /**
     * 商户升级，付款获得url
     *
     * @param merchantId
     * @param businessOrderNo 业务订单号
     * @param amount
     * @param businessCallbackUrl 业务方回调url
     * @return
     */
    Pair<Integer, String> generateMerchantUpgradeUrl(long merchantId, String businessOrderNo, BigDecimal amount, String businessCallbackUrl);

    /**
     * 生成订单
     *
     * @param merchantId
     * @param amount
     * @param appId
     * @return
     */
    long generateBusinessOrder(long merchantId, String amount, String appId);
    /**
     * 动态码收款
     *
     * @param businessOrderId
     * @param channel  通道
     * @param isDynamicCode 是否是动态码付款
     * @return
     */
    Pair<Integer, String> codeReceipt(long businessOrderId, int channel, String appId, boolean isDynamicCode);


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
     * 将交易订单标记为 处理中
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    void markPayHandling(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order);

    /**
     * 用户付款，商户入账
     *  1.入账到商户的账户待结算余额
     *  2.入账到手续费账户待结算余额
     *
     * @param orderId
     * @param merchant
     */
    long merchantRecorded(long orderId, MerchantInfo merchant);

    /**
     * 支付手续费结算 到 代理商等 待结算账户
     *
     * @param order
     * @param merchantId
     */
    void poundageSettle(Order order, long merchantId);

    /**
     * 手续费分账，生成代理商结算单记录
     *
     * @param accountId
     * @param dealer
     * @param settleDate
     * @param settleAmount
     * @return
     */
    long generateDealerSettlementRecord(long accountId, Dealer dealer, Date settleDate, BigDecimal settleAmount);

    /**
     * 手续费分账，生成商户结算单记录
     *
     * @param accountId
     * @param merchant
     * @param settleDate
     * @param settleAmount
     * @return
     */
    long generateMerchantSettlementRecord(long accountId, MerchantInfo merchant, Date settleDate, BigDecimal settleAmount);

    /**
     * 手续费分账，代理商待结算入可用余额
     *
     * @param accountId
     * @param settleAmount
     * @param order
     * @param settlementRecordId
     */
    void dealerRecordedAccount(long accountId, BigDecimal settleAmount, Order order, long settlementRecordId);

    /**
     * 手续费分账，商户待结算入可用余额
     *
     * @param accountId
     * @param settleAmount
     * @param order
     * @param settlementRecordId
     */
    void merchantRecordedAccount(long accountId, BigDecimal settleAmount, Order order, long settlementRecordId, String remark);

    /**
     * 支付手续费结算 到 代理商等 待结算账户
     *
     * @param order
     * @param merchantId
     */
    void merchantUpgradePoundageSettle(Order order, long merchantId);

    /**
     * 首次支付
     *
     * @param businessOrderId
     * @param channel
     * @param creditBankCardId
     * @param appId
     * @return
     */
    Pair<Integer,String> firstUnionPay(long businessOrderId, int channel, long creditBankCardId, String appId);

    /**
     * 再次支付
     *
     * @param businessOrderId
     * @param channel
     * @param expireDate
     * @param cvv
     * @param appId
     * @return
     */
    Pair<Integer,String> againUnionPay(long businessOrderId, int channel, String expireDate, String cvv, long creditBankCardId, String appId);

    /**
     * 快捷支付
     *
     * @param merchantId  商户ID
     * @param amount      金额
     * @param channel     渠道
     * @param creditBankCardId  信用卡ID
     * @param appId
     * @return
     */
    Pair<Integer,String> unionPay(long orderId, long merchantId, String amount, int channel, long creditBankCardId, String appId);

    /**
     * 确认支付
     *
     * @param orderId
     * @param code
     * @return
     */
    Pair<Integer,String> confirmUnionPay(long orderId, String code);
}
