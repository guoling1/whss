package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/6/10.
 */
@Data
public class XmmsCallBackRequest {
    /**
     * 入网信息
     */
    private String msg;
    /**
     * 渠道商户编号
     */
    private String merchantCode;
    /**
     * 支付方式
     */
    private String payWay;
    /**
     * 活动编号
     */
    private String channelMerchantCode;
    /**
     * 结算方式
     */
    private String settleType;
    /**
     * 商户编号
     */
    private String merchantNo;
    /**
     * 回调结果
     */
    private String status;
}
