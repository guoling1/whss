package com.jkm.hss.helper.response;

import com.jkm.hss.helper.JkmApiErrorCode;
import lombok.Data;

/**
 * Created by yuxiang on 2017-08-13.
 */
@Data
public class CreateApiOrderResponse {

    /**
     *
     */
    private String amount;
    /**
     *
     */
    private String orderNum;

    /**
     *
     */
    private String qrCode;
    /**
     *
     */
    private String returnCode;
    /**
     *
     */
    private String returnMsg;
    /**
     *
     */
    private String sign;

    public void setResponse(JkmApiErrorCode errorCode){
        this.returnCode = errorCode.getErrorCode();
        this.returnMsg = errorCode.getErrorMessage();
    }
}
