package com.jkm.hss.helper.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/1/20.
 */
@Data
public class ListSettleAuditRecordResponse {


    private long id;
    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 上级代理编号
     */
    private String dealerNo;

    /**
     * 上级代理名称
     */
    private String dealerName;

    /**
     * 业务线（结算产品）
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;


    private String appName;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 交易笔数
     */
    private int tradeNumber;

    /**
     * 结算总金额
     */
    private BigDecimal settleAmount;

    /**
     * 对账状态
     *
     * {@link com.jkm.hss.settle.enums.EnumAccountCheckStatus}
     */
    private int checkedStatus;

    private String checkedStatusValue;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.settle.enums.EnumSettleStatus}
     */
    private int settleStatus;

    private String settleStatusValue;

    /**
     * 结算日期
     */
    private Date settleDate;
}
