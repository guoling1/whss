package com.jkm.hss.merchant.helper.response;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-24 10:36
 */
@Data
public class TradeQueryResult {
    /**
     * 交易类型
     * 1.微信扫码S
     * 2.微信二维码N
     * 3.微信H5
     * 4.收银台H
     * 5.快捷收款B
     * 6.支付宝二维码Z
     */
    private String tradeType;
    /**
     *三方系统订单号
     */
    private String outTradeNo;
    /**
     * 支付状态
     * SUCCESS--支付成功
     * REFUND--转入退款
     * NOTPAY--未支付
     * CLOSED--已关闭
     * REVOKED--已撤销
     * USERPAYING--用户支付中
     * NOPAY--未支付(确认支付超时)
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     */
    private String tradeState;
    /**
     * 商户订单号
     */
    private String orderId;
}
