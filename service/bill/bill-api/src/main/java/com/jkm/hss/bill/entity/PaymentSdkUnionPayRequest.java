package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 *
 *
 * 支付中心 快捷 预下单入参
 */
@Data
public class PaymentSdkUnionPayRequest {
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
     * 后台回调地址
     */
    private String notifyUrl;

    /**
     * 商户名称
     */
    private String merName;

    /**
     * 总金额
     */
    private String totalAmount;

    /**
     * 持卡人姓名
     */
    private String cardByName;
    /**
     * 信用卡号
     */
    private String cardByNo;

    /**
     * 有效期
     */
    private String expireDate;

    /**
     * cvv2
     */
    private String cardCvv;

    /**
     * 证件号码
     */
    private String cerNumber;

    /**
     * 手机号
     */
    private String mobile;
}
