package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/7/4.
 */
@Data
public class PcGenerateOrderRequest {

    /**
     * 店铺id
     */
    private long shopId;

    /**
     * 交易金额
     */
    private String amount;
}
