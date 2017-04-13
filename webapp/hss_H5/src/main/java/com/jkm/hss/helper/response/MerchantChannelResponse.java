package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yuxiang on 2017-03-31.
 */
@Data
public class MerchantChannelResponse {


    private String payMethod;

    private String channelName;

    private String settleType;

    private String channelRate;

    private String limitAmount;

    private int channelSign;
}
