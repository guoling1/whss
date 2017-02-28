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
     * 编号
     */
    private String userNo;

    /**
     * 名称
     */
    private String userName;

    /**
     * 账户类型id
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int userType;

    /**
     * 账户类型
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private String userTypeValue;

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
