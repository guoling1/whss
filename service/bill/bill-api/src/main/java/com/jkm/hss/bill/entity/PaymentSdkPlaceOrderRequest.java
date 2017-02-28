package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/23.
 *
 * 请求支付中心下单 参数
 */
@Data
public class PaymentSdkPlaceOrderRequest {

    /**
     * 系统标识
     */
    private String appId;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 支付通道
     *
     * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private String channel;

    /**
     * 商品描述
     */
    private String goodsDescribe;

    /**
     * 前端回调地址
     */
    private String returnUrl;

    /**
     * 后台回调地址
     */
    private String notifyUrl;

    /**
     * 商户名称
     */
    private String merName;

    /**
     * 商户编号
     */
    private String merNo;

    /**
     * 总金额
     */
    private String totalAmount;


    //////**卡盟通道所需参数**//////////

    /**
     * 收款行联行号
     */
    private String bankCode;

    /**
     * 入账卡号 DES加密
     */
    private String cardNo;

    /**
     *入帐卡对应姓名
     *
     */
    private String payerName;

    /**
     *入帐卡对应身份证号 DES加密
     *
     */
    private String idCardNo;
}
