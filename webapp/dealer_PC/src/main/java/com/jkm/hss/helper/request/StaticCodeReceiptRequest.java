package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/16.
 */
@Data
public class StaticCodeReceiptRequest {

    /**
     * 代理商id
     */
    private long merchantId;

    /**
     * 总金额
     */
    private String totalFee;

    /**
     * 支付通道
     */
    private int payChannel;
}
