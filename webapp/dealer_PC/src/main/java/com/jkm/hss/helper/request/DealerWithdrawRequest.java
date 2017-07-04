package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/19.
 */
@Data
public class DealerWithdrawRequest {

    /**
     * 提现金额
     */
    private String amount;

    /**
     * 提现渠道
     *
     * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private int channel;


    /**
     * 提现验证码
     */
    private String code;
}
