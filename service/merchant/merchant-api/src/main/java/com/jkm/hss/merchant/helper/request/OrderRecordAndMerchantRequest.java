package com.jkm.hss.merchant.helper.request;

import lombok.Data;

import java.util.List;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-14 12:56
 */
@Data
public class OrderRecordAndMerchantRequest {
    /**
     * 页数
     */
    private int page;
    /**
     * 每页条数
     */
    private int size;
    private int offset;
    /**
     * 商户id
     */
    private Long merchantId;
    /**
     * 商户名称
     */
    private String name;
    /**
     * 支付状态
     */
    private String payResult;
    /**
     * 注册手机号
     */
    private String mobile;
    /**
     * 银行卡号后四位
     */
    private String bankNoShort;
    /**
     * 交易起始时间
     */
    private String startDate;
    /**
     * 交易结束时间
     */
    private String endDate;
    /**
     * 支付完成起始时间
     */
    private String payStartDate;
    /**
     * 支付完成结束时间
     */
    private String payEndDate;
    /**
     * 流水号
     */
    private String orderId;
    /**
     * 支付状态
     */
    private List<String> payResults;
}
