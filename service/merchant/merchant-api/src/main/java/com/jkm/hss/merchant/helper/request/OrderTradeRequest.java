package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by Thinkpad on 2016/12/20.
 */
@Data
public class OrderTradeRequest {

    /**
     * 店铺shop编码
     */
    private String globalId;

    /**
     * hsy商户名
     */
    private String shortName;

    /**
     * 商户编码
     */
    private String markCode;

    /**
     * 产品
     */
    private String appId;

    /**
     * 一级代理商id
     */
    private long firstDealerId;

    /**
     * 二级代理商id
     */
    private long secondDealerId;

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 业务订单号
     */
    private String businessOrderNo;

    /**
     * 支付流水号
     */
    private String sn;

    /**
     * 所属一级代理商
     */
    private String proxyName;

    /**
     * 所属二级代理商
     */
    private String proxyName1;

    /**
     * 页数
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 条数
     */
    private Integer offset;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 查询条件：设备编号（交易单号）
     */
    private String orderNo;

    /**
     * 查询条件：商户号
     */
    private Long merchantId;

    /**
     * 查询条件：商户名称
     */
    private String merchantName;

    /**
     * 查询条件：交易金额（小）
     */
    private Double lessTotalFee;

    /**
     * 查询条件：交易金额（大）
     */
    private Double moreTotalFee;

    /**
     * 查询条件：订单状态
     * 1：待支付，， 3：支付失败，4：支付成功，5：提现中，6：提现成功，7：充值成功，8：充值失败
     */
    private Integer status;

    /**
     * 查询条件：支付方式
     *
     */
    private String payType;

    /**
     *结算状态1：待结算，2：结算中，3：已结算
     */
    private Integer settleStatus;

    /**
     * 支付渠道标识（101， 102， 103）
     *
     *
     */
    private int payChannelSign;

}
