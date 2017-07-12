package com.jkm.hss.bill.entity.callback;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/9.
 *
 * 重复支付退款回调
 */
@Data
public class PaymentSdkRefundCallbackResponse {

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 请求时间
     */
    private String requestTime;

    /**
     * 退款金额
     */
    private String amount;
}
