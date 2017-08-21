package com.jkm.hss.helper.request;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.util.ApiMD5Util;
import com.jkm.hss.helper.JKMTradeServiceException;
import com.jkm.hss.helper.JkmApiErrorCode;
import com.jkm.hss.helper.JkmApiMerConstants;
import com.jkm.hss.helper.ValidateUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by yuxiang on 2017-08-13.
 */
@Data
public class CreateApiOrderRequest {

    /**
     * 接口类型
     * WX_SCANCODE_JSAPI微信公众号
     * Alipay_SCANCODE_JSAPI 支付宝服务窗
     */
    private String trxType;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 商户订单号
     */
    private String orderNum;

    /**
     * 金额
     */
    private String amount;

    /**
     * 订单描述
     */
    private String goodsName;

    /**
     * 支付回调url
     */
    private String callbackUrl;

    private String pageCallbackUrl;

    /**
     * 签名
     */
    private String sign;

    public boolean isSignCorrect(JSONObject jsonObject, String md5Key, String sign){

        return ApiMD5Util.verifySign(jsonObject,md5Key,sign);
    }

    public void validate() {
        if (StringUtils.isEmpty(trxType)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "接口类型错误");
        }
        if (StringUtils.isEmpty(merchantNo)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "商户号不能为空");
        }
        if (StringUtils.isEmpty(JkmApiMerConstants.keyOf(merchantNo))){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "商户号不存在");
        }
        if (StringUtils.isEmpty(orderNum)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "商户订单号不能为空");
        }
        if (ValidateUtils.isNull(amount) || !ValidateUtils.isDouble(amount.toString())){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "订单金额错误");
        }
        if (new BigDecimal(amount).compareTo(new BigDecimal("3000")) == 1){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "单笔支付金额超限");
        }
        if (StringUtils.isEmpty(goodsName)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "订单描述不能为空");
        }
        if (StringUtils.isEmpty(callbackUrl)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "支付通知地址不能为空");
        }
        if (StringUtils.isEmpty(pageCallbackUrl)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "用户支付IP不能为空");
        }
        if (StringUtils.isEmpty(sign)){
            throw new JKMTradeServiceException(JkmApiErrorCode.COMMON_ERROR, "签名不能为空");
        }
    }

}
