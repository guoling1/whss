package com.jkm.hss.helper.response;

import com.jkm.hss.helper.JkmApiErrorCode;
import lombok.Data;

/**
 * Created by yuxiang on 2017-08-14.
 */
@Data
public class QueryApiOrderResponse {

    private String trxType;

    private String returnCode;


    private String returnMsg;


    private String orderNum;


    private String amount;


    private String status;

    private String sign;

    public void setResponse(JkmApiErrorCode errorCode){
        this.returnCode = errorCode.getErrorCode();
        this.returnMsg = errorCode.getErrorMessage();
    }
}
