package com.jkm.hss.dealer.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2017/5/16.
 */
@Data
public class BranchAccountDetailResponse {

    /**
     * 账户id
     */
    private long id;

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
