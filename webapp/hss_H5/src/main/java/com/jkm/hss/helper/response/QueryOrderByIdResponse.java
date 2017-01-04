package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Data
public class QueryOrderByIdResponse {

    /**
     * 订单id
     */
    private long orderId;

    /**
     * 交易金额
     */
    private String amount;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品描述
     */
    private String goodsDescribe;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 支付状态
     *
     * {@link com.jkm.hss.bill.enums.EnumOrderStatus}
     */
    private int payStatus;

    /**
     * 支付状态值
     */
    private String payStatusValue;

    /**
     * 支付方式
     *
     * {@link com.jkm.hss.bill.enums.EnumPaymentType}
     */
    private String payType;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleStatus}
     */
    private int settleStatus;

    /**
     * 结算状态值
     */
    private String settleStatusValue;
}
