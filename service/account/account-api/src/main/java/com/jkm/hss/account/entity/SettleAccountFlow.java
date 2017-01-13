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
     * 账户id
     */
    private long accountId;

    /**
     * 业务号（交易订单号）
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

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.account.enums.EnumSettleStatus}
     */
    private int settleStatus;
}
