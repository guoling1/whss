package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/13.
 */
@Data
public class UnionPayRequest {

    /**
     * 订单id
     */
    private long orderId;

    /**
     * 支付通道
     */
    private int payChannel;
}
