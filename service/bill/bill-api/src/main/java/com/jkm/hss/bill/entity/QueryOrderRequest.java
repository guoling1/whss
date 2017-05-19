package com.jkm.hss.bill.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by zhangbin on 2017/5/18.
 */
@Data
public class QueryOrderRequest {

    /**
     * 支付方式
     */
    private int paymentMethod;

    /**
     * 支付渠道标识（101， 102， 103）
     *
     * {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private List<Integer> payChannelSign;

    /**
     * 业务订单号
     */
    private String orderNo;

    /**
     * 交易订单号
     */
    private String tradeOrderNo;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 收款商户
     */
    private String merchantName;

    /**
     * 所属一级代理商
     */
    private String proxyName;

    /**
     * 所属二级代理商
     */
    private String proxyName1;

    /**
     * 订单状态
     */
    private int status;

    /**
     * 订单创建时间：开始时间
     */
    private String startTime;

    /**
     * 订单创建时间：结束时间
     */
    private String endTime;

    /**
     * 交易时间
     */
    private String paySuccessTime;

    /**
     * 交易时间
     */
    private String paySuccessTime1;

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


}
