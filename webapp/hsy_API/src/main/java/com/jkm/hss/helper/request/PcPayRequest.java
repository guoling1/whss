package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Data
public class PcPayRequest {

    /**
     * 订单id
     */
    private long orderId;

    /**
     * 授权码
     */
    private String authCode;
}
