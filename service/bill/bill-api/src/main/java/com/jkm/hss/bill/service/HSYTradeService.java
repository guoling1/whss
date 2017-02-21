package com.jkm.hss.bill.service;


import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/1/17.
 */
public interface HSYTradeService {

    /**
     * 查询提现页面所需信息app
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    String getWithdrawInfo(String dataParam, AppParam appParam);

    /**
     * app交易记录
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    String tradeList(String dataParam, AppParam appParam);

    /**
     * app收款
     *
     * @param paramData
     * @param appParam
     * @return
     */
    String appReceipt(String paramData, AppParam appParam);

    /**
     * 收款
     *
     * @param totalAmount
     * @param channel
     * @param shopId  店铺id
     * @param appId
     * @return
     */
    Pair<Integer, String> receipt(String totalAmount, final int channel, final long shopId, final String appId);


    /**
     * 处理支付中心， 支付结果回调
     *
     * @param paymentSdkPayCallbackResponse
     */
    void handleHsyPayCallbackMsg(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse);

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
     *
     * @param orderId
     * @param shop
     */
    void recorded(long orderId, AppBizShop shop);

    /**
     * 分账
     *
     * @param order
     * @param shop
     */
    void paySplitAccount(Order order, AppBizShop shop);


    /**
     * app提现
     *
     * @param paramData
     * @param appParam
     * @return
     */
    String appWithdraw(String paramData, AppParam appParam);

    /**
     * 提现
     *
     * @param accountId
     * @param totalAmount
     * @param channel
     * @return
     */
    Pair<Integer, String> withdraw(long accountId, String totalAmount, int channel, String appId);

    /**
     * 提现实现
     *
     * @param shop
     * @param playMoneyOrderId
     * @return
     */
    Pair<Integer, String> withdrawImpl(AppBizShop shop, long playMoneyOrderId, EnumUpperChannel playMoneyChannel);

    /**
     * 提现回调
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    void handleHsyWithdrawCallbackMsg(PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse);

    /**
     * 提现分账
     *
     * @param order
     * @param shop
     */
    void withdrawSplitAccount(Order order, AppBizShop shop);
}
