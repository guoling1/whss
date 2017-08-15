package com.jkm.api.helper.responseparam;

import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/15.
 *
 * 快捷预下单 返回参数
 */
@Data
public class PreQuickPayResponse {

    /**
     * 商户编号
     */
    private String merchantNo;
    /**
     * 商户订单号
     */
    private String orderNo;
    /**
     * 交易订单号
     */
    private String tradeOrderNo;
    /**
     * 商户请求时间
     */
    private String merchantReqTime;
    /**
     * 订单金额
     */
    private String orderAmount;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 结算状态
     */
    private String settleStatus;
    /**
     * 错误码
     */
    private String returnCode;
    /**
     * 错误描述
     */
    private String returnMsg;
    /**
     * 签名
     */
    private String sign;

    public String createSign(final String merchantNo) {
        return SdkSignUtil.sign(SdkSerializeUtil.convertObjToMap(this), merchantNo);
    }
}
