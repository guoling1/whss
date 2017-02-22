package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Data
public class QueryMerchantPayOrdersResponse {

    /**
     * 交易单id
     */
    private long orderId;

    /**
     * 交易金额
     */
    private String amount;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付状态
     *
     * {@link com.jkm.hss.bill.enums.EnumOrderStatus}
     */
    private int payStatus;

    /**
     * 支付状态值
     */
    private String payStatusValue;

    /**
     * 时间
     */
    private Date datetime;
}
