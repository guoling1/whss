package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/24.
 */
@Data
public class PaymentSdkDaiFuResponse {
    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 打款流水号
     */
    private String sn;

    /**
     * 提现金额
     */
    private String withdrawAmount;

    /**
     * 提现完成时间
     */
    private String withdrawSuccessTime;

    /**
     * 成功标识
     *
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int status;

    /**
     * 错误信息
     */
    private String message;
}
