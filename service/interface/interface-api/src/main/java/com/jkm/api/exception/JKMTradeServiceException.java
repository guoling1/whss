package com.jkm.api.exception;

import com.jkm.api.enums.JKMTradeErrorCode;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
@Data
public class JKMTradeServiceException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public JKMTradeServiceException(final JKMTradeErrorCode jkmTradeErrorCode){
        super(jkmTradeErrorCode.getErrorMessage());
        this.errorCode = jkmTradeErrorCode.getErrorCode();
        this.errorMessage = jkmTradeErrorCode.getErrorMessage();
    }

    public JKMTradeServiceException(final JKMTradeErrorCode jkmTradeErrorCode, final String errorMessage){
        super(errorMessage);
        this.errorCode = jkmTradeErrorCode.getErrorCode();
        this.errorMessage = errorMessage;
    }
}
