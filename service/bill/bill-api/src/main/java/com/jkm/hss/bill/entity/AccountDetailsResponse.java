package com.jkm.hss.bill.entity;

import com.jkm.hss.account.enums.EnumAccountFlowType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 公司账户明细
 */


@Data
public class AccountDetailsResponse {

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 业务号（交易号）
     */
    private String orderNo;

    /**
     * 发生前余额
     */
    private BigDecimal beforeAmount;

    /**
     * 发生后余额
     */
    private BigDecimal afterAmount;

    /**
     * 支出金额
     */
    private BigDecimal outAmount;

    /**
     * 收入金额
     */
    private BigDecimal incomeAmount;

    /**
     * 发生时间
     */
    private Date changeTime;

    /**
     * 类型
     *
     * {@link EnumAccountFlowType}
     */
    private int type;

    /**
     * 备注
     */
    private String remark;
}
