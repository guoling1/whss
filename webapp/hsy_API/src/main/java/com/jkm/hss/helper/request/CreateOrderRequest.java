package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/6/13.
 *
 * 好收银创建订单
 */
@Data
public class CreateOrderRequest {

    /**
     * 渠道
     */
    private int channel;
    /**
     * 店铺id
     */
    private long shopId;
    /**
     * 会员标识
     */
    private String memberId;
    /**
     * 二维码
     */
    private String code;
}
