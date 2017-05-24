package com.jkm.hss.helper.request;

import lombok.Data;


/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Data
public class DynamicCodePayRequest {

    /**
     * 订单id
     */
    private long orderId;

    /**
     * 支付通道
     */
    private int payChannel;
}
