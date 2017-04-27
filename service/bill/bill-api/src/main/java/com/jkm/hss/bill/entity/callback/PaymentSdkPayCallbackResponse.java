package com.jkm.hss.bill.entity.callback;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/23.
 *
 * 网关返回的支付结果参数
 */
@Data
public class PaymentSdkPayCallbackResponse {

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 支付流水号
     */
    private String sn;

    /**
     * 支付金额
     */
    private String payAmount;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付完成时间
     */
    private String paySuccessTime;

    /**
     * 状态
     *
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int status;

    /**
     * 信息
     */
    private String message;

    /**
     * 银行流水号
     */
    private String bankTradeNo;

    /**
     * 交易卡类型
     *
     * {@link com.jkm.hss.account.enums.EnumBankType}
     */
    private String tradeCardType;

    /**
     * 交易卡号
     */
    private String tradeCardNo;

    /**
     * 支付宝/微信订单号
     */
    private String wechatOrAlipayOrderNo;

}
