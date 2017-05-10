package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 账户的待结算金额变动 记录
 *
 * tb_settle_account_flow
 *
 */
@Data
public class SettleAccountFlow extends BaseEntity {

    /**
     * 流水号
     */
    private String flowNo;

    /**
     * 结算单记录id
     */
    private long settlementRecordId;

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 结算审核记录id
     */
    private long settleAuditRecordId;

    /**
     * 账户所属用户类型
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int accountUserType;

    /**
     * 业务号（交易订单号）
     */
    private String orderNo;

    /**
     * 退款单号
     */
    private String refundOrderNo;

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
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 结算日期
     */
    private Date settleDate;

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
