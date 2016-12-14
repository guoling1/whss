package com.jkm.hss.merchant.helper.request;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-25 16:28
 */
@Data
public class TradeRequest {
    /**
     * 商户id
     */
    private long merchantId;
    /**
     * 总金额(订单总金额，单位为元)
     */
    private String totalFee;
    /**
     * 当前时间
     */
    private String timeStamp;
    /**
     * 商品描述
     */
    private String body;
    /**
     * 订单流水号(下游发起订单请求时的订单号)
     */
    private String payNum;
    /**
     * 下有商户名称
     */
    private String subMerName;
    /**
     * 下游商户号
     */
    private String subMerNo;
    /**
     * 前台回调地址
     */
    private String returnUrl;
    /**
     * 后台通知地址
     */
    private String notifyUrl;
    /**
     * 交易类型
     */
    private int payChannel;
}
