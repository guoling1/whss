package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/5/5.
 *
 * 退款 入参
 */
@Data
public class PaymentSdkRefundRequest {
    /**
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;
    /**
     * 退款单号
     */
    private String refundOrderNo;
    /**
     * 交易单号
     */
    private String orderNo;
    /**
     * 金额
     */
    private String amount;
}
