package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Data
public class MerchantChannelRateRequest {
    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 产品id
     */
    private long productId;

    /**
     * 通道唯一标识
     */
    private int channelTypeSign;
}
