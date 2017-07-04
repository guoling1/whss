package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/5/5.
 *
 * 退款 出参
 */
@Data
public class PaymentSdkRefundResponse {

    /**
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int code;
    /**
     * 网关信息
     */
    private String message;
    /**
     * 退款单号
     */
    private String refundOrderNo;
    /**
     * 退款成功
     */
    private String successTime;
}
