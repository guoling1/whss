package com.jkm.hss.bill.helper.responseparam;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/5/16.
 */
@Data
public class RefundProfitResponse {

    /**
     * 交易单号
     */
    private String tradeOrderNo;
    /**
     * 退款单号
     */
    private String refundOrderNo;
    /**
     * 返回码
     *
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int code;
    /**
     * 信息
     */
    private String message;
}
