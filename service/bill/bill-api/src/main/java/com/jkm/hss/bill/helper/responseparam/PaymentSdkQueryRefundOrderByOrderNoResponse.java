package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/8.
 */
@Data
public class PaymentSdkQueryRefundOrderByOrderNoResponse {
    /**
     * 退款单号
     */
    private String orderNo;
    /**
     * 流水号
     */
    private String sn;
    /**
     * 退款金额
     */
    private String amount;
    /**
     * 退款发起时间
     */
    private Date refundStartTime;
    /**
     * 状态
     */
    private String status;
    /**
     * 退款完成时间
     */
    private Date refundSuccessTime;
    /**
     * 信息
     */
    private String message;
}
