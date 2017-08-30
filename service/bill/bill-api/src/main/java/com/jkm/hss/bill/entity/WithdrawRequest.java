package com.jkm.hss.bill.entity;

import lombok.Data;

@Data
public class WithdrawRequest {



    /**
     * 提现时间
     */
    private String createTimes;

    /**
     * 商户id
     */
    private long idm;

    /**
     * 代理商id
     */
    private long idd;

    /**
     * 提现状态
     */
    private String withdrawStatus;

    /**
     * 交易状态
     */
    private int status;

    /**
     * 账户名称
     */
    private String userName;

    /**
     * 业务订单号（下游）
     */
    private String businessOrderNo;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 页数
     */
    private Integer pageNo;
    /**
     * 每页条数
     */
    private Integer pageSize;
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
}
