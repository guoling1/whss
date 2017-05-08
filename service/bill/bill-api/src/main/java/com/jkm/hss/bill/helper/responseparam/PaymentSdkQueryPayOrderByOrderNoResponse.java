package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/8.
 */
@Data
public class PaymentSdkQueryPayOrderByOrderNoResponse {

    /**
     * 流水号
     */
    private String sn;
    /**
     * 支付金额
     */
    private String payAmount;
    /**
     * 支付发起时间
     */
    private Date payStartTime;
    /**
     * 支付状态
     */
    private String payStatus;
    /**
     * 成功时间
     */
    private Date paySuccessTime;
    /**
     * 支付渠道
     */
    private String payChannel;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 渠道方
     */
    private String upperChannel;
    /**
     * 渠道信息
     */
    private String message;
}
