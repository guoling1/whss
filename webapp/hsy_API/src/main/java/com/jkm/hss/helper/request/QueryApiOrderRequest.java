package com.jkm.hss.helper.request;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.util.ApiMD5Util;
import lombok.Data;

/**
 * Created by yuxiang on 2017-08-14.
 */
@Data
public class QueryApiOrderRequest {

    private String trxType;

    private String merchantNo;

    private String orderNum;

    private String amount;

    private String sign;

    public boolean isSignCorrect(JSONObject jsonObject, String md5Key, String sign){

        return ApiMD5Util.verifySign(jsonObject,md5Key,sign);
    }

    public void validate() {}

}
