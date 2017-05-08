package com.jkm.hss.bill.helper;

import com.google.common.base.Preconditions;
import lombok.experimental.UtilityClass;
import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@UtilityClass
public final class PaymentSdkConstants {

    public static final String APP_ID;

    /**
     * 支付前端回调url
     */
    public static final String SDK_PAY_RETURN_URL;

    /**
     * 支付后台回调url
     */
    public static final String SDK_PAY_NOTIFY_URL;

    /**
     * 提现后台回调url
     */
    public static final String SDK_PAY_WITHDRAW_NOTIFY_URL;

    /**
     * 下单接口
     */
    public static final String SDK_PAY_PLACE_ORDER;

    /**
     *  快捷下单-发送验证码
     */
    public static final String SDK_PAY_UNIONPAY_PREPARE;

    /**
     * 快捷支付-确认支付
     */
    public static final String SDK_PAY_UNIONPAY_CONFRIM;

    /**
     * 提现接口
     */
    public static final String SDK_PAY_WITHDRAW;

    /**
     * 商户入网结果查询接口
     */
    public static final String SDK_PAY_QUERYIN;

    /**
     * 退款地址
     */
    public static final String SDK_PAY_REFUND;

    /**
     * 查询支付流水
     */
    public static final String SDK_QUERY_PAY_ORDER_URL;

    /**
     * 查询退款流水
     */
    public static final String SDK_QUERY_REFUND_ORDER_URL;

    static {
        final PaymentSdkConfig config = getConfig();
        APP_ID= config.appId();
        Preconditions.checkState(!StringUtils.isEmpty(APP_ID), "加载好收收appId失败");
        SDK_PAY_RETURN_URL= config.sdkPayReturnUrl();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_RETURN_URL), "加载支付中心支付前端回调url失败");
        SDK_PAY_NOTIFY_URL= config.sdkPayNotifyUrl();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_NOTIFY_URL), "加载支付中心支付后台回调url失败");
        SDK_PAY_WITHDRAW_NOTIFY_URL= config.sdkPayWithdrawNotifyUrl();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_WITHDRAW_NOTIFY_URL), "加载支付中心提现回调url失败");
        SDK_PAY_PLACE_ORDER= config.sdkPayPlaceOrder();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_PLACE_ORDER), "加载支付中心下单url失败");
        SDK_PAY_WITHDRAW= config.sdkPayWithdraw();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_WITHDRAW), "加载支付中心提现url失败");
        SDK_PAY_QUERYIN= config.sdkPayQueryIn();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_QUERYIN), "加载支付中心查询商户入网url失败");
        SDK_PAY_UNIONPAY_PREPARE = config.sdkPayUnionPayPrepare();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_UNIONPAY_PREPARE), "加载支付中心快捷支付-预下单url失败");
        SDK_PAY_UNIONPAY_CONFRIM = config.sdkPayUnionPayConfirm();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_UNIONPAY_CONFRIM), "加载支付中心快捷支付-确认下单url失败");
        SDK_PAY_REFUND = config.sdkPayRefund();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_PAY_REFUND), "加载支付中心退款url失败");
        SDK_QUERY_PAY_ORDER_URL = config.sdkQueryPayOrderUrl();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_QUERY_PAY_ORDER_URL), "加载支付中心查询支付流水url失败");
        SDK_QUERY_REFUND_ORDER_URL = config.sdkQueryRefundOrderUrl();
        Preconditions.checkState(!StringUtils.isEmpty(SDK_QUERY_REFUND_ORDER_URL), "加载支付中心查询退款流水url失败");
    }

    private static PaymentSdkConfig getConfig() {
        return ConfigCache.getOrCreate(PaymentSdkConfig.class);
    }

    @Config.Sources("classpath:sdk/payment-sdk.properties")
    private interface PaymentSdkConfig extends Config{

        @Key("app.id")
        @DefaultValue("hss")
        String appId();

        /**
         * 支付中心支付前端回调url
         */
        @Key("payment.sdk.pay.return.url")
        @DefaultValue("http://hss.qianbaojiajia.com/sqb/success/")
        String sdkPayReturnUrl();

        /**
         * 支付中心支付后台回调url
         */
        @Key("payment.sdk.pay.notify.url")
        @DefaultValue("http://hss.qianbaojiajia.com/callback/pay")
        String sdkPayNotifyUrl();

        /**
         * 支付中心提现回调url
         */
        @Key("payment.sdk.pay.withdraw.notify.url")
        @DefaultValue("http://hss.qianbaojiajia.com/callback/withdraw")
        String sdkPayWithdrawNotifyUrl();

        /**
         * 支付中心下单url
         */
        @Key("payment.sdk.pay.place.order")
        @DefaultValue("http://pay.qianbaojiajia.com/pay/placeOrder")
        String sdkPayPlaceOrder();

        /**
         * 支付中心提现url
         */
        @Key("payment.sdk.pay.withdraw.url")
        @DefaultValue("http://pay.qianbaojiajia.com/daiFu/withdraw")
        String sdkPayWithdraw();

        /**
         * 支付中心 查询商户入网url
         */
        @Key("payment.sdk.pay.query.url")
        @DefaultValue("http://pay.qianbaojiajia.com/km/merchant/query")
        String sdkPayQueryIn();

        /**
         * 无卡下单
         *
         * @return
         */
        @Key("payment.sdk.pay.unionpay.prepare")
        @DefaultValue("http://pay.qianbaojiajia.com/pay/fastPlaceOrder")
        String sdkPayUnionPayPrepare();

        /**
         * 无卡确认支付
         *
         * @return
         */
        @Key("payment.sdk.pay.unionpay.confirm")
        @DefaultValue("http://pay.qianbaojiajia.com/pay/confirmPlaceOrder")
        String sdkPayUnionPayConfirm();

        /**
         * 退款
         *
         * @return
         */
        @Key("payment.sdk.pay.refund.url")
        @DefaultValue("http://pay.qianbaojiajia.com/pay/refund")
        String sdkPayRefund();

        /**
         * 查询支付流水
         *
         * @return
         */
        @Key("payment.sdk.pay.query.pay.order.url")
        @DefaultValue("http://pay.qianbaojiajia.com/order/queryPayByOrderNo")
        String sdkQueryPayOrderUrl();

        /**
         * 查询退款流水
         *
         * @return
         */
        @Key("payment.sdk.pay.query.pay.order.url")
        @DefaultValue("http://pay.qianbaojiajia.com/order/queryPayByOrderNo")
        String sdkQueryRefundOrderUrl();
    }
}
