package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yuxiang on 2017-08-15.
 */
@Data
public class MerApiCallBackResp {


    private String returnCode;

    private String returnMsg;

    private String orderNum;

    private String tradeOrderNo;

    private String paySuccessTime;

    private String sign;
}
