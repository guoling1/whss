package com.jkm.hss.bill.helper.requestparam;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/5/8.
 */
@Data
public class PaymentSdkQueryRefundOrderByOrderNoRequest {

    /**
     * 退款单号
     */
    private String orderNo;
}
