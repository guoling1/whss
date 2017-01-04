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
}
