package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yuxiang on 2017-03-31.
 */
@Data
public class MerchantChannelResponse {

    private int payMethodCode;

    private String payMethod;

    private String channelName;

    private String settleType;

    private String channelRate;

    private String fee;

    private String limitAmount;

    private int channelSign;

    private int recommend;
}
