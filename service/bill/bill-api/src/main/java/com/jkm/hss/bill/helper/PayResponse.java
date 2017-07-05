package com.jkm.hss.bill.helper;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/5/16.
 */
@Data
public class PayResponse {

    /**
     * 业务订单号
     */
    private String businessOrderNo;
    /**
     * 交易订单号
     */
    private String tradeOrderNo;
    /**
     * 交易订单id
     */
    private long tradeOrderId;
    /**
     * 支付url
     */
    private String url;
    /**
     * 返回码
     *
     * {@link com.jkm.hss.bill.enums.EnumBasicStatus}
     */
    private int code;
    /**
     * 返回信息
     */
    private String message;
}
