package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 *
 */
@Data
public class CompanyPrifitRequest {

    /**
     * 账户id
     */
    private long aeceiptMoneyAccountId;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 分润方名称
     */
    private String receiptMoneyUserName;

    /**
     * 代理商编号
     */
    private String markCode;

    /**
     * 一级代理商名称
     */
    private String proxyName;

    /**
     * 一级代理商名称
     */
    private String proxyName1;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 收益类型
     * {@link com.jkm.hss.account.enums.EnumSplitBusinessType}
     */
    private String businessType;

    /**
     * 账户id
     */
    private long accId;

    /**
     * 收款账户id
     */
    private long receiptMoneyAccountId;

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
}
