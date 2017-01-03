package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by Thinkpad on 2016/12/20.
 */
@Data
public class OrderTradeRequest {
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
     *101.微信扫码
     * 102.支付宝扫码
     * 103.银联
     */
    private Integer payType;

    /**
     *结算状态1：待结算，2：结算中，3：已结算
     */
    private Integer settleStatus;
}
