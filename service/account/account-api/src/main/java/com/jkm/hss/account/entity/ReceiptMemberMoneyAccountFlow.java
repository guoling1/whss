package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/10.
 *
 * 商户收会员款账户流水
 *
 * tb_receipt_member_money_account_flow
 */
@Data
public class ReceiptMemberMoneyAccountFlow extends BaseEntity {
    /**
     * 商户收会员款账户id
     */
    private long accountId;

    /**
     * 流水号
     */
    private String flowNo;

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
