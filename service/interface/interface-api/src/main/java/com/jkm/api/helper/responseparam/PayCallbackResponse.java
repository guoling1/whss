package com.jkm.api.helper.responseparam;

import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/17.
 *
 * 支付回调参数
 */
@Data
public class PayCallbackResponse {

    /**
     * 代理商编号
     */
    @SdkSerializeAlias(signSort = 3, needSign = true)
    private String dealerMarkCode;
    /**
     * 商户编号
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String merchantNo;
    /**
     * 商户订单号
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String orderNo;
    /**
     * 交易单号
     */
    @SdkSerializeAlias(signSort = 6, needSign = true)
    private String tradeOrderNo;
    /**
     * 商户请求时间
     */
    @SdkSerializeAlias(signSort = 7, needSign = true)
    private String merchantReqTime;
    /**
     * 订单金额
     */
    @SdkSerializeAlias(signSort = 8, needSign = true)
    private String orderAmount;
    /**
     * 卡号
     */
    @SdkSerializeAlias(signSort = 9, needSign = true)
    private String cardNo;
    /**
     * 订单状态
     */
    @SdkSerializeAlias(signSort = 10, needSign = true)
    private String orderStatus;
    /**
     * 错误码
     */
    @SdkSerializeAlias(signSort = 1, needSign = true)
    private String returnCode;
    /**
     * 错误描述
     */
    @SdkSerializeAlias(signSort = 2, needSign = true)
    private String returnMsg;
    /**
     * 签名
     */
    private String sign;


    public String createSign(final String key) {
        return SdkSignUtil.sign2(this, key);
    }
}
