package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/24.
 */
@Data
public class StaticCodePayRequest {

    /**
     * 店铺id
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

    /**
     * 会员id
     */
    private String memberId;
}
