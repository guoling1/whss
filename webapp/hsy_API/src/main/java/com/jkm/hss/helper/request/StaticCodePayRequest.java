package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/24.
 */
@Data
public class StaticCodePayRequest {

    /**
     * 代理商id
     */
    private long shopId;

    /**
     * 总金额
     */
    private String totalFee;

    /**
     * 支付通道
     */
    private int payChannel;
}
