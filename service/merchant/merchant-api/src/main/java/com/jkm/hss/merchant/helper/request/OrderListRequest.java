package com.jkm.hss.merchant.helper.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Thinkpad on 2016/12/20.
 */
@Data
public class OrderListRequest {
    /**
     * 页数
     */
    private int page;
    /**
     * 每页条数
     */
    private int size;
    /**
     * 条数
     */
    private int offset;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 查询条件：设备编号（订单号）
     */
    private String orderId;

    /**
     * 查询条件：商户号
     */
    private Long merchantId;

    /**
     * 查询条件：商户名称
     */
    private String subName;

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
     */
    private String payResult;

    /**
     * 查询条件：业务
     *
     *101.微信扫码
     * 102.支付宝扫码
     * 103.银联
     */
    private int payChannel;

    /**
     * 查询条件：电话
     */
    private String mdMobile;

    /**
     * 订单状态
     */
    private List<String> payResults;

    /**
     *结算状态 0,已经算 1.未结算
     */
    private int settleStatus=-1;
}
