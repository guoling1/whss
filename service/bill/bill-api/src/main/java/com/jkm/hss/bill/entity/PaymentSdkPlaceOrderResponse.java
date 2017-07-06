package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Data
public class PaymentSdkPlaceOrderResponse {

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 成功标识
     *
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int code;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 返回的支付url
     */
    private String payUrl;

    /**
     * 支付要素
     */
    private String payInfo;
}
