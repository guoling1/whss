package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/19.
 */
@Data
public class DealerWithdrawRequest {

    /**
     * 账户id
     */
    private long accountId;

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
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;
}
