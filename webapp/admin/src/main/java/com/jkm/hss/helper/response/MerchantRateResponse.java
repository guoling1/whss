package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yuxiang on 2017-03-01.
 */
@Data
public class MerchantRateResponse {

    private String channelName;

    private String merchantRate;

    private String withdrawMoney;

    private String entNet;

    private String remarks;
}
